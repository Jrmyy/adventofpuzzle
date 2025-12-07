def parse_input() -> tuple[list[int], list[int]]:
    with open("day_1.txt") as f_in:
        lines = f_in.readlines()

    first, second = [], []
    for l in lines:
        i, j = [int(p.strip()) for p in l.split("   ")]
        first.append(i)
        second.append(j)

    return sorted(first), sorted(second)


def run_part_one() -> int:
    first, second = parse_input()
    return sum([abs(j - first[idx]) for idx, j in enumerate(second)])


def run_part_two() -> int:
    first, second = parse_input()
    return sum(i * second.count(i) for i in first)


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
