"""Draws the Key Concepts maps: python concepts/maps/make_maps.py
Edit the node and edge lists below, run it, and commit the SVGs it writes."""
import math
from html import escape

OUT = str(__import__('pathlib').Path(__file__).parent) + '/'
H = 44


def node(nid, x, y, name, sub, w=124, accent=False, mono=False):
    stroke = 'var(--c-accent)' if accent else 'var(--c-border)'
    fill = 'var(--c-raised)' if accent else 'var(--c-surface)'
    fam = 'JetBrains Mono, monospace' if mono else 'Source Serif 4, Georgia, serif'
    size = 13 if mono else 14
    return (f'<g class="kc-node" data-kc="{nid}"><a href="#kc-{nid}">'
            f'<title>{escape(name)}: {escape(sub)}</title>'
            f'<rect x="{x}" y="{y}" width="{w}" height="{H}" rx="8" fill="{fill}" stroke="{stroke}" stroke-width="2"/>'
            f'<text x="{x + 12}" y="{y + 19}" font-family="{fam}" font-size="{size}" font-weight="700" fill="var(--c-ink)">{escape(name)}</text>'
            f'<text x="{x + 12}" y="{y + 35}" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">{escape(sub)}</text>'
            f'</a></g>')


def edge(ids, pts, label=None, lx=0, ly=0, anchor='middle'):
    """pts: list of (x, y); the arrowhead goes on the last point."""
    (x1, y1), (x2, y2) = pts[-2], pts[-1]
    d = math.hypot(x2 - x1, y2 - y1)
    ux, uy = (x2 - x1) / d, (y2 - y1) / d
    bx, by = x2 - 9 * ux, y2 - 9 * uy
    px, py = -uy * 5, ux * 5
    path = 'M' + ' L'.join(f'{x},{y}' for x, y in pts)
    s = (f'<g class="kc-edge" data-kc="{ids}">'
         f'<path d="{path}" fill="none" stroke="var(--c-muted)" stroke-width="1.5" opacity="0.7"/>'
         f'<polyline points="{bx + px:.1f},{by + py:.1f} {x2},{y2} {bx - px:.1f},{by - py:.1f}" fill="none" stroke="var(--c-muted)" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" opacity="0.7"/>')
    if label:
        s += (f'<text x="{lx}" y="{ly}" text-anchor="{anchor}" font-family="JetBrains Mono, monospace" '
              f'font-size="10" fill="var(--c-accent)">{escape(label)}</text>')
    return s + '</g>'


def svg(w, h, title, desc, parts):
    return (f'<svg viewBox="0 0 {w} {h}" role="img" xmlns="http://www.w3.org/2000/svg">'
            f'<title>{escape(title)}</title><desc>{escape(desc)}</desc>' + ''.join(parts) + '</svg>')


# ---------------------------------------------------------------- module 1
m1 = [
    edge('class object', [(322, 64), (322, 128)], 'describes', 330, 101, 'start'),
    edge('constructor object', [(144, 152), (258, 152)], 'builds', 201, 145),
    edge('class static', [(384, 42), (498, 42)], 'owns', 441, 35),
    edge('reference object', [(500, 152), (386, 152)], 'points at', 443, 145),
    edge('object field', [(300, 174), (225, 238)], 'knows', 256, 210, 'end'),
    edge('object method', [(344, 174), (359, 238)], 'does', 360, 210, 'start'),
    edge('reference null', [(600, 174), (650, 238)], 'may be', 632, 204, 'start'),
    edge('list reference', [(532, 240), (548, 176)], 'holds', 534, 212, 'end'),
    edge('foreach list', [(636, 340), (565, 286)], 'visits', 610, 318, 'start'),
    edge('field encapsulation', [(212, 284), (262, 338)], 'private', 232, 318, 'end'),
    edge('method encapsulation', [(362, 284), (330, 338)], 'checks', 352, 318, 'start'),
    edge('encapsulation invariant', [(365, 362), (428, 362)], 'protects', 397, 355),
    edge('final field', [(100, 340), (176, 286)], 'fixes', 132, 318, 'end'),
    node('class', 260, 20, 'Class', 'the description'),
    node('static', 500, 20, 'static', 'one per class', mono=True),
    node('constructor', 20, 130, 'Constructor', 'runs once, at new'),
    node('object', 260, 130, 'Object', 'state + behaviour', accent=True),
    node('reference', 500, 130, 'Reference', 'an arrow to it'),
    node('field', 150, 240, 'Field', 'what it knows'),
    node('method', 300, 240, 'Method', 'what it does'),
    node('list', 470, 240, 'List', 'many references', mono=True),
    node('null', 616, 240, 'null', 'no object', w=92, mono=True),
    node('final', 20, 340, 'final', 'set once', w=110, mono=True),
    node('encapsulation', 225, 340, 'Encapsulation', 'private + checks', w=140),
    node('invariant', 430, 340, 'Invariant', 'always true'),
    node('foreach', 590, 340, 'for-each', 'visits each item', w=116, mono=True),
]
open(OUT + 'module-1.svg', 'w').write(svg(
    720, 400, 'How the Module 1 concepts connect',
    'A class describes objects and owns its static members. A constructor builds an object. '
    'A reference points at an object or may be null, and a list holds many references, visited by a for-each loop. '
    'An object knows things through its fields and does things through its methods. Private fields and checking methods '
    'make up encapsulation, which protects the invariant. final fixes a field once.', m1))

# ---------------------------------------------------------------- module 2
W = 200


def col_label(x, text, week, kc):
    return (f'<g class="kc-col" data-kc="{kc}"><text x="{x}" y="22" font-family="Inter, sans-serif" font-size="12" font-weight="600" fill="var(--c-muted)">{escape(text)}</text>'
            f'<text x="{x + W}" y="22" text-anchor="end" font-family="JetBrains Mono, monospace" font-size="10" fill="var(--c-muted)">{escape(week)}</text></g>')


A, B, C = 20, 260, 500
ya = [44, 124, 204, 294, 374]
m2 = [
    col_label(A, 'Who does the work', 'weeks 5-6', 'responsibility'),
    col_label(B, 'Same or not', 'week 5', 'equality'),
    col_label(C, 'A fixed set of values', 'week 6', 'enum'),
    # column A
    edge('srp responsibility', [(A + 100, ya[0] + H), (A + 100, ya[1] - 2)], 'one per class', A + 108, ya[0] + H + 22, 'start'),
    edge('responsibility delegation', [(A + 100, ya[1] + H), (A + 100, ya[2] - 2)], 'others ask the owner', A + 108, ya[1] + H + 22, 'start'),
    edge('separation model-ui', [(A + 100, ya[3] + H), (A + 100, ya[4] - 2)], 'splits a program into', A + 108, ya[3] + H + 22, 'start'),
    node('srp', A, ya[0], 'Single Responsibility', 'one reason to change', w=W, accent=True),
    node('responsibility', A, ya[1], 'Responsibility', 'a job a class owns', w=W),
    node('delegation', A, ya[2], 'Delegation', 'ask, do not reach in', w=W),
    node('separation', A, ya[3], 'Separation of concerns', 'rules apart from talk', w=W),
    node('model-ui', A, ya[4], 'Model and user interface', 'decides / talks', w=W),
    # column B
    edge('value-entity equality', [(B + 100, 44 + H), (B + 100, 124 - 2)], 'decides the rule of', B + 108, 44 + H + 22, 'start'),
    edge('equality hashcode', [(B + 100, 124 + H), (B + 100, 204 - 2)], 'must agree with', B + 108, 124 + H + 22, 'start'),
    edge('identity equality', [(B + W, 316), (B + W + 14, 316), (B + W + 14, 146), (B + W + 2, 146)], 'implies', B + W + 10, 274, 'end'),
    node('value-entity', B, 44, 'Value and entity', 'which fields decide', w=W),
    node('equality', B, 124, 'Equality', 'equals(): same thing', w=W, accent=True),
    node('hashcode', B, 204, 'Hash code', 'hashCode(), the bucket', w=W),
    node('identity', B, 294, 'Identity', '==: same object', w=W),
    # column C
    edge('enum type-safety', [(C + 100, 124), (C + 100, 44 + H + 2)], 'gives', C + 108, 44 + H + 22, 'start'),
    edge('enum enum-constant', [(C + 100, 124 + H), (C + 100, 204 - 2)], 'lists every', C + 108, 124 + H + 22, 'start'),
    edge('enum-constant switch-expression', [(C + 100, 204 + H), (C + 100, 284 - 2)], 'all covered by', C + 108, 204 + H + 22, 'start'),
    node('type-safety', C, 44, 'Type safety', 'typos do not compile', w=W),
    node('enum', C, 124, 'Enumeration', 'enum: a fixed list', w=W, accent=True),
    node('enum-constant', C, 204, 'Enum constant', 'an object, made once', w=W),
    node('switch-expression', C, 284, 'Switch expression', 'covers every case', w=W),
]
open(OUT + 'module-2.svg', 'w').write(svg(
    720, 430, 'How the Module 2 concepts connect',
    'Three groups. Who does the work: the Single Responsibility Principle gives each class one responsibility, and '
    'other objects reach it through delegation; separation of concerns splits a program into a model and a user interface. '
    'Same or not: value and entity decide the rule of equality, equality must agree with the hash code, and identity implies equality. '
    'A fixed set of values: an enumeration gives type safety and lists every enum constant, which a switch expression must cover.', m2))
print('maps written')
