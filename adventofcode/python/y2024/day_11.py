def parse_input() -> dict[int, int]:
    with open("day_11.txt") as f_in:
        keys = f_in.read().strip().split(" ")
    return {int(k): 1 for k in keys}


def blink(times: int) -> int:
    stones = parse_input()
    for i in range(0, times):
        new_stones = {}
        for stone, cnt in stones.items():
            s = str(stone)
            if stone == 0:
                transformed = [1]
            elif len(s) % 2 == 0:
                l = len(s) // 2
                left, right = s[:l], s[l:]
                transformed = [int(left), int(right)]
            else:
                transformed = [str(int(stone) * 2024)]
            for ns in transformed:
                new_stones[ns] = new_stones.get(ns, 0) + cnt
        stones = new_stones
    return sum(stones.values())


def run_part_one() -> int:
    return blink(25)


def run_part_two() -> int:
    return blink(75)


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
