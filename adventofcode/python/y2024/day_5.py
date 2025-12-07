def parse_input() -> tuple[dict[int, set[int]], list[list[int]]]:
    with open("day_5.txt") as f_in:
        lines = f_in.readlines()

    rules = {}
    updates = []

    for line in lines:
        if "|" in line:
            before, after = line.split("|")
            before, after = int(before), int(after)
            if not rules.get(before):
                rules[before] = set()
            rules[before].add(after)

        if "," in line:
            updates.append([int(i) for i in line.split(",")])

    return rules, updates


def run_part_one() -> int:
    middle = 0
    rules, updates = parse_input()
    for update in updates:
        if _is_valid(update, rules):
            middle += update[len(update) // 2]
    return middle


def run_part_two() -> int:
    middle = 0
    rules, updates = parse_input()
    for update in updates:
        if not _is_valid(update, rules):
            fixed = _fix(update, rules)
            middle += fixed[len(fixed) // 2]
    return middle


def _is_valid(update: list[int], rules: dict[int, set[int]]) -> bool:
    for idx, i in enumerate(update):
        rule = rules[i]
        if not set(update[idx + 1 :]).issubset(rule):
            return False
    return True


def _fix(update: list[int], rules: dict[int, set[int]]) -> list[int]:
    fixed = []
    while len(fixed) != len(update):
        candidates = [i for i in update if i not in fixed]
        for i in candidates:
            remaining = [j for j in candidates if i != j]
            if set(remaining).issubset(rules[i]):
                fixed.append(i)
    return fixed


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
