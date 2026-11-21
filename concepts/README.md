# Key Concepts data

The **Key Concepts** tab is built from the files in this folder by
`hooks/key_concepts.py`. The pages in `docs/key-concepts/` only hold short
placeholders such as `<!-- kc:cards module-1 -->`.

| File | What it holds |
|---|---|
| `module-1.yml`, `module-2.yml` | One entry per concept card |
| `commonly-confused.yml` | The side-by-side pairs |
| `maps/module-*.svg` | The concept map at the top of each module page |
| `maps/make_maps.py` | Draws those maps; edit it and run it to change a map |
| `figures/*.svg` | Diagrams used by some pairs |

## Adding a concept

Add an entry to the module's file. The fields are described at the top of
`module-1.yml`. `lecture` must be the page that teaches it.

## Releasing

Nothing extra to do. A card, a pair or a map box appears on the public site
as soon as its `lecture` page is released, that is, removed from
`draft_docs` in `mkdocs.yml`. `mkdocs serve` shows everything, drafts
included.

## Module 3 and 4

Create `module-3.yml` with `module: module-3` and
`page: key-concepts/module-3.md`, add the page with the same placeholders as
`module-2.md`, add it to the nav, and add `module-3` to the `modules` list in
`commonly-confused.yml`.
