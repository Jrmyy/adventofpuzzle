directions = {"<": (-1, 0), ">": (1, 0), "^": (0, -1), "v": (0, 1)}


def debug(warehouse: dict[tuple[int, int], str], robot: tuple[int, int]) -> None:
    mx, my = max(warehouse, key=lambda coord: coord[0])[0] + 1, max(warehouse, key=lambda coord: coord[1])[1] + 1
    warehouse_rows = [["."] * mx for _ in range(my)]
    for p, c in warehouse.items():
        if p == robot:
            c = "@"
        warehouse_rows[p[1]][p[0]] = c
    print("\n".join(["".join(r) for r in warehouse_rows]))


def parse_input(wider: bool) -> tuple[dict[tuple[int, int], str], tuple[int, int], str]:
    with open("day_15.txt") as f_in:
        lines = f_in.readlines()

    warehouse = {}
    moves = ""
    start = (-1, -1)

    for y, line in enumerate(lines):
        if line.startswith("#"):
            for x, c in enumerate(list(line.strip())):
                positions_to_add = [(2 * x, y), (2 * x + 1, y)] if wider else [(x, y)]
                if c == "@":
                    start = positions_to_add[0]
                    c = "."
                match c:
                    case ".":
                        chars_to_add = [".", "."]
                    case "#":
                        chars_to_add = ["#", "#"]
                    case _:
                        chars_to_add = ["[", "]"] if wider else ["O"]
                for idx, p in enumerate(positions_to_add):
                    warehouse[p] = chars_to_add[idx]
        elif line == "":
            continue
        else:
            moves += line.strip()

    return warehouse, start, moves


def run_part_one() -> int:
    warehouse, robot, moves = parse_input(False)
    for move in moves:
        direction = directions[move]
        next_robot = (robot[0] + direction[0], robot[1] + direction[1])
        if warehouse[next_robot] == "#":
            continue
        if warehouse[next_robot] == ".":
            robot = next_robot
            continue

        to_push = []
        box = next_robot
        while warehouse[box] == "O":
            to_push.append(box)
            box = (box[0] + direction[0], box[1] + direction[1])

        if warehouse[box] == "#":
            continue
        robot = next_robot
        warehouse[next_robot] = "."
        for box in to_push:
            warehouse[(box[0] + direction[0], box[1] + direction[1])] = "O"

    res = 0
    for p, c in warehouse.items():
        if c == "O":
            res += 100 * p[1] + p[0]

    return res


def run_part_two():
    warehouse, robot, moves = parse_input(True)
    for move in moves:
        direction: tuple[int, int] = directions[move]
        next_robot = (robot[0] + direction[0], robot[1] + direction[1])
        if warehouse[next_robot] == "#":
            continue
        if warehouse[next_robot] == ".":
            robot = next_robot
            continue

        if move in "><":
            to_push = {}
            box_part = next_robot
            while warehouse[box_part] in "[]":
                to_push[box_part] = warehouse[box_part]
                box_part = (box_part[0] + direction[0], box_part[1] + direction[1])

            if warehouse[box_part] == "#" or len(to_push) % 2 != 0:
                continue

            robot = next_robot
            warehouse[next_robot] = "."
            for box, v in to_push.items():
                warehouse[(box[0] + direction[0], box[1] + direction[1])] = v
        else:
            box_part = next_robot
            other_box_part = (
                (box_part[0] + 1, box_part[1]) if warehouse[box_part] == "[" else (box_part[0] - 1, box_part[1])
            )
            to_push = {
                box_part: warehouse[box_part],
                other_box_part: warehouse[other_box_part],
            }
            can_move = True
            while True:
                should_stop = False
                changed = False

                for box_part in list(to_push.keys()):
                    moved_box_part = (box_part[0] + direction[0], box_part[1] + direction[1])
                    c = warehouse[moved_box_part]
                    if c == "#":
                        # It means that a part of box will reach a wall, which makes the whole move impossible
                        should_stop = True
                        can_move = False
                        break
                    if c in "[]":
                        # It means that a part of a box is pushing another part of another box and the moved box part
                        # will take the place of an existing box. We need to make the whole structure move.
                        if moved_box_part not in to_push:
                            changed = True
                            to_push[moved_box_part] = c
                            moved_other_box_part = (
                                (moved_box_part[0] + 1, moved_box_part[1])
                                if c == "["
                                else (moved_box_part[0] - 1, moved_box_part[1])
                            )
                            to_push[moved_other_box_part] = warehouse[moved_other_box_part]

                if should_stop or not changed:
                    break

            if can_move:
                robot = next_robot
                for box_part in to_push:
                    warehouse[box_part] = "."
                for box_part, v in to_push.items():
                    warehouse[(box_part[0] + direction[0], box_part[1] + direction[1])] = v

    res = 0
    for p, c in warehouse.items():
        if c == "[":
            res += 100 * p[1] + p[0]

    return res


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
