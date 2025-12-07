import re


def parse_input() -> list[list[str]]:
    with open("day_23.txt") as f_in:
        raw_instructions = f_in.readlines()

    return [instruction.strip().split(" ") for instruction in raw_instructions]


def run_part_one() -> int:
    registers = {}
    idx = 0
    instructions = parse_input()
    num_mul = 0

    def _get_value(v: str) -> int:
        if re.match(r"-?\d", v):
            return int(v)
        return registers.get(v, 0)

    while 0 <= idx < len(instructions):
        command, *args = instructions[idx]
        offset = 1

        match command:
            case "set":
                value = _get_value(args[1])
                registers[args[0]] = value
            case "sub":
                value = _get_value(args[1])
                registers[args[0]] = registers.get(args[0], 0) - value
            case "mul":
                value = _get_value(args[1])
                registers[args[0]] = registers.get(args[0], 0) * value
                num_mul += 1
            case "jnz":
                possible_offset = _get_value(args[1])
                control_value = _get_value(args[0])
                if control_value != 0:
                    offset = possible_offset
            case _:
                raise NotImplementedError(f"{command} not implemented.")

        idx += offset

    return num_mul


def _is_prime(n: int) -> bool:
    for i in range(2, (n // 2) + 1):
        if n % i == 0:
            return False
    return True


def run_part_two() -> int:
    """
    Assembly code translated to python:
    while True:
        d = 2
        f = 1

        while g != 0
            e = 2

            while g != 0:
                g = d * e - b
                if g == 0:
                    f = 0
                e += 1
                g = e - b

            d += 1
            g = d - b

        h += 1
        g = b - c
        if g == 0:
            # end of program
            return h

        b += 17

        And then
    for b in range(b0, c + 1, 17):
        f = 1
        for d in range(2, b + 1):
            for e in range(2, b + 1):
                if d * e == b:
                    f = 0
        if f == 0:
            h += 1

    return h

    And then
    for b in range(b0, c + 1, 17):
        if not _is_prime(b):
            h += 1
    return h
    """
    b0 = 93 * 100 + 100000
    c = b0 + 17000
    h = 0

    for b in range(b0, c + 1, 17):
        if not _is_prime(b):
            h += 1

    return h


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
