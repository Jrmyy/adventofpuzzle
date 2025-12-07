import re
import sys


width = 101
height = 103


def parse_input() -> list[tuple[int, ...]]:
    with open("day_14.txt") as f_in:
        lines = f_in.readlines()
    robots = []
    for line in lines:
        x, y, vx, vy = [int(i) for i in re.findall(r"(-?\d+)", line)]
        robots.append((x, y, vx, vy))
    return robots


def debug(robots: list[tuple[int, ...]]) -> None:
    lines = []
    for y in range(101):
        line = []
        for x in range(101):
            c = "#" if any([rx == x and ry == y for (rx, ry, _, __) in robots]) else " "
            line.append(c)
        lines.append("".join(line))
    print("\n".join(lines))


def calculate_safety_factor(robots: list[tuple[int, ...]]) -> int:
    q1, q2, q3, q4 = 0, 0, 0, 0
    for robot in robots:
        x, y, _, __ = robot
        if x < width // 2 and y < height // 2:
            q1 += 1
        elif x < width // 2 and y > height // 2:
            q2 += 1
        elif x > width // 2 and y < height // 2:
            q3 += 1
        elif x > width // 2 and y > height // 2:
            q4 += 1
    return q1 * q2 * q3 * q4


def run_part_one() -> int:
    robots = parse_input()
    for _ in range(100):
        robots = [((x + vx) % width, (y + vy) % height, vx, vy) for (x, y, vx, vy) in robots]
    return calculate_safety_factor(robots)


def run_part_two() -> int:
    robots = parse_input()
    m = sys.maxsize
    j = -1
    easter_egg = []
    for i in range(10_000):
        robots = [((x + vx) % width, (y + vy) % height, vx, vy) for (x, y, vx, vy) in robots]
        # My intuition was that we had to calculate the safety factor for a reason. I thought that the Easter egg
        # will have a lot of robots in the middle, leading to the minimum safety factor. The minimum factor also means
        # that most of the robots are in the same quadrant which means there will be an arrangement occurring in one of
        # the quadrant, probably the Easter egg.
        if (sf := calculate_safety_factor(robots)) < m:
            easter_egg = robots
            m = sf
            j = i
    debug(easter_egg)
    return j + 1


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
