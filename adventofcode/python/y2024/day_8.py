from collections import namedtuple

Point = namedtuple("Point", ["x", "y"])


def parse_input() -> tuple[int, int, list[tuple[Point, str]]]:
    antennas = []
    with open("day_8.txt") as f_in:
        lines = f_in.readlines()

    for y, line in enumerate(lines):
        for x, c in enumerate(line.strip()):
            if c != ".":
                antennas.append((Point(x=x, y=y), c))
    return len(lines[0].strip()), len(lines), antennas


def run_part_one() -> int:
    return _count_anti_nodes()


def run_part_two() -> int:
    return _count_anti_nodes(mode="all")


def _count_anti_nodes(mode: str = "perfect") -> int:
    mx, my, antennas = parse_input()
    r = range(1, 2) if mode == "perfect" else range(0, my)

    anti_nodes = set()
    for i, (ap1, ac1) in enumerate(antennas):
        for ap2, ac2 in antennas[i + 1 :]:
            if ac1 != ac2:
                continue
            dx, dy = ap1.x - ap2.x, ap1.y - ap2.y
            for d in r:
                anti_nodes.add(Point(x=ap1.x + d * dx, y=ap1.y + d * dy))
                anti_nodes.add(Point(x=ap2.x - d * dx, y=ap2.y - d * dy))

    return len([p for p in anti_nodes if p.x in range(0, mx) and p.y in range(0, my)])


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
