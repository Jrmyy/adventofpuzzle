def parse_input() -> list[list[int]]:
    with open("day_2.txt") as f_in:
        lines = f_in.readlines()

    return [[int(p.strip()) for p in l.split(" ")] for l in lines]


def _is_safe(r: list[int]) -> bool:
    direction = None
    for idx, i in enumerate(r[:-1]):
        j = r[idx + 1]

        curr_direction = "dec" if i - j > 0 else "inc"
        if direction is None:
            direction = curr_direction
        if direction != curr_direction:
            return False

        if not (3 >= abs(i - j) >= 1):
            return False

    return True


def run_part_one() -> int:
    reports = parse_input()
    return len([r for r in reports if _is_safe(r)])


def run_part_two() -> int:
    reports = parse_input()
    return len([r for r in reports if any(_is_safe(r[0:k] + r[k + 1 :]) for k in range(len(r))) or _is_safe(r)])


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
