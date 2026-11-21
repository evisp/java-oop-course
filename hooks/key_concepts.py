"""Key Concepts: builds the concept cards, maps and pairs from concepts/*.yml.

A page asks for a piece with an HTML comment on a line of its own:

    <!-- kc:map module-1 -->      the concept map, concepts/maps/module-1.svg
    <!-- kc:toolbar -->           the Read / Test yourself switch
    <!-- kc:cards module-1 -->    every card of concepts/module-1.yml
    <!-- kc:pairs -->             every pair of concepts/commonly-confused.yml
    <!-- kc:count module-1 -->    the number of visible cards ("all", or "pairs")

A card or pair only appears once the lecture it points at is released, that
is, once that page is no longer listed in draft_docs. `mkdocs serve` shows
everything, so drafts can be previewed together with their concepts.
"""

import html
import re
from pathlib import Path

import yaml
from mkdocs.utils import get_relative_url

_SERVE = {"on": False}
_DATA = {}
_PLACEHOLDER = re.compile(r"^<!--\s*kc:(\w+)\s*([\w-]*)\s*-->\s*$", re.M)
_SVG_GROUP = re.compile(r'<g class="kc-(?:node|edge|col)" data-kc="([^"]+)">.*?</g>', re.S)


# ---------------------------------------------------------------- MkDocs events

def on_startup(command, dirty):
    _SERVE["on"] = command == "serve"


def on_config(config):
    _DATA["root"] = Path(config.config_file_path).parent / "concepts"
    return config


def on_files(files, config):
    """Load the data once per build, so edits show up on every reload."""
    root = _DATA["root"]
    modules = {}
    for path in sorted(root.glob("module-*.yml")):
        data = yaml.safe_load(path.read_text(encoding="utf-8"))
        modules[data["module"]] = data
    pairs = yaml.safe_load((root / "commonly-confused.yml").read_text(encoding="utf-8"))
    _DATA["modules"] = modules
    _DATA["pairs"] = pairs
    _DATA["files"] = files

    index = {}
    for data in modules.values():
        for concept in data["concepts"]:
            if concept["id"] in index:
                raise ValueError(f"Key Concepts: duplicate id {concept['id']!r}")
            index[concept["id"]] = dict(concept, page=data["page"])
    _DATA["index"] = index
    return files


def on_page_markdown(markdown, page, config, files):
    if "<!-- kc:" not in markdown:
        return markdown

    def replace(match):
        kind, arg = match.group(1), match.group(2)
        if kind == "map":
            return _map(arg)
        if kind == "toolbar":
            return _toolbar()
        if kind == "cards":
            return _cards(arg, page)
        if kind == "pairs":
            return _pairs(page)
        if kind == "count":
            return str(_count(arg))
        raise ValueError(f"Key Concepts: unknown placeholder kc:{kind}")

    return _PLACEHOLDER.sub(replace, markdown)


def on_page_content(html_text, page, config, files):
    # Inline counts sit inside paragraphs, which the line-based pattern above
    # does not reach.
    return re.sub(r"<!--\s*kc:count\s*([\w-]+)\s*-->",
                  lambda m: str(_count(m.group(1))), html_text)


# ---------------------------------------------------------------- visibility

def _released(src):
    file = _DATA["files"].get_file_from_path(src)
    if file is None:
        raise ValueError(f"Key Concepts: lecture page not found: {src}")
    if _SERVE["on"]:
        return file.inclusion.is_in_serve()
    return file.inclusion.is_included()


def _visible_concepts(module):
    return [c for c in _DATA["modules"][module]["concepts"] if _released(c["lecture"])]


def _visible_pairs():
    return [p for p in _DATA["pairs"]["pairs"] if _released(p["lecture"])]


def _count(arg):
    if arg == "pairs":
        return len(_visible_pairs())
    if arg == "all":
        return sum(len(_visible_concepts(m)) for m in _DATA["modules"])
    return len(_visible_concepts(arg))


# ---------------------------------------------------------------- small helpers

def _inline(text):
    """Escape text and turn `code` and **bold** into HTML, for raw HTML spots."""
    out = html.escape(text.strip(), quote=False)
    out = re.sub(r"`([^`]+)`", r"<code>\1</code>", out)
    return re.sub(r"\*\*([^*]+)\*\*", r"<strong>\1</strong>", out)


def _fence(code):
    return "``` java\n" + code.rstrip("\n") + "\n```"


def _link(page, src, anchor=""):
    target = _DATA["files"].get_file_from_path(src)
    url = get_relative_url(target.url, page.url)
    return url + anchor


# ---------------------------------------------------------------- pieces

def _map(module):
    path = _DATA["root"] / "maps" / f"{module}.svg"
    svg = path.read_text(encoding="utf-8").replace("\n", " ")
    visible = {c["id"] for c in _visible_concepts(module)}

    def keep(match):
        ids = match.group(1).split()
        return match.group(0) if all(i in visible for i in ids) else ""

    svg = _SVG_GROUP.sub(keep, svg)
    return ('<div class="kc-map">' + svg +
            '<p class="kc-map__hint">Click a box to jump to its card.</p></div>')


def _toolbar():
    return (
        '<div class="kc-toolbar" role="group" aria-label="How to show the cards">'
        '<div class="kc-switch">'
        '<button type="button" class="kc-mode is-active" data-mode="read" aria-pressed="true">Read</button>'
        '<button type="button" class="kc-mode" data-mode="test" aria-pressed="false">Test yourself</button>'
        '</div>'
        '<span class="kc-progress" hidden></span>'
        '<button type="button" class="kc-reset" hidden>Turn all back</button>'
        '</div>'
    )


def _cards(module, page):
    data = _DATA["modules"][module]
    concepts = _visible_concepts(module)
    out = []
    for week in sorted({c["week"] for c in concepts}):
        group = [c for c in concepts if c["week"] == week]
        title = data["weeks"].get(week, "")
        out.append(f"## Week {week} · {title} {{ #week-{week} }}\n")
        out.append('<div class="kc-grid" markdown>\n')
        for concept in group:
            out.append(_card(concept, page))
        out.append("</div>\n")
    return "\n".join(out)


def _card(c, page):
    term_html = _inline(c["term"])
    front = (
        '<div class="kc-face kc-front" aria-hidden="true">'
        f'<span class="kc-week">Week {c["week"]}</span>'
        f'<p class="kc-front__term">{term_html}</p>'
        '<p class="kc-front__prompt">Explain it in your own words, then turn the card.</p>'
        '</div>'
    )
    parts = [
        f'<div class="kc-card" tabindex="0" markdown>',
        front,
        '<div class="kc-face kc-back" markdown>',
        "",
        f'### {c["term"]} {{ #kc-{c["id"]} }}',
        "",
        c["definition"].strip(),
        "",
    ]
    if c.get("example"):
        parts += [_fence(c["example"]), ""]
    if c.get("confused"):
        parts += [f'<p class="kc-confused"><span class="kc-label">Often confused with</span>'
                  f'{_inline(c["confused"])}</p>', ""]

    chips = []
    for rid in c.get("related", []):
        other = _DATA["index"].get(rid)
        if other is None:
            raise ValueError(f"Key Concepts: {c['id']} relates to unknown id {rid!r}")
        if not _released(other["lecture"]):
            continue
        href = f"#kc-{rid}" if other["page"] == page.file.src_uri else _link(page, other["page"], f"#kc-{rid}")
        chips.append(f'<a class="kc-chip" href="{href}">{_inline(other["term"])}</a>')

    foot = '<div class="kc-foot">'
    if chips:
        foot += '<span class="kc-chips">' + "".join(chips) + "</span>"
    foot += (f'<a class="kc-source" href="{_link(page, c["lecture"])}">'
             f'{html.escape(c["source"])}</a></div>')
    parts += [foot, "", "</div>", "</div>", ""]
    return "\n".join(parts)


def _pairs(page):
    pairs = _visible_pairs()
    names = _DATA["pairs"]["modules"]
    out = []
    for module, name in names.items():
        group = [p for p in pairs if p["module"] == module]
        if not group:
            continue
        number = module.split("-")[1]
        out.append(f"## Module {number} · {name} {{ #{module} }}\n")
        for p in group:
            out.append(_pair(p, page))
    return "\n".join(out)


def _side(side):
    return "\n".join([
        '<div class="kc-side" markdown>',
        f'<p class="kc-side__term">{_inline(side["term"])}</p>',
        "",
        side["text"].strip(),
        "",
        _fence(side["code"]),
        "",
        "</div>",
    ])


def _pair(p, page):
    parts = [f'### {p["title"]} {{ #vs-{p["id"]} }}', "", '<div class="kc-pair" markdown>', ""]
    if p.get("figure"):
        svg = (_DATA["root"] / "figures" / p["figure"]).read_text(encoding="utf-8").replace("\n", " ")
        parts += [f'<div class="kc-pair__figure">{svg}</div>', ""]
    parts += [
        '<div class="kc-pair__sides" markdown>', "",
        _side(p["a"]), "",
        '<div class="kc-vs" aria-hidden="true">vs</div>', "",
        _side(p["b"]), "",
        "</div>", "",
        f'<p class="kc-diff"><span class="kc-label">The difference</span>{_inline(p["difference"])}</p>', "",
        '<details class="kc-check">',
        f'<summary><span class="kc-label">Check yourself</span>{_inline(p["check"]["question"])}</summary>',
        f'<p>{_inline(p["check"]["answer"])}</p>',
        "</details>", "",
        f'<p class="kc-pair__source"><a href="{_link(page, p["lecture"])}">{html.escape(p["source"])}</a></p>', "",
        "</div>", "",
    ]
    return "\n".join(parts)
