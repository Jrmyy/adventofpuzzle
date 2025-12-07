def parse_input() -> list[tuple[int, ...]]:
    with open("day_24.txt") as f_in:
        return [tuple(map(int, line.strip().split("/"))) for line in f_in.readlines()]


def run_part_one() -> int:
    components = parse_input()
    strongest_bridge = []
    max_strength = 0

    def build_bridge(bridge: list[tuple[int, ...]], port: int) -> None:
        nonlocal max_strength
        nonlocal strongest_bridge

        for component in components:
            if port in component:
                next_port = component[0] if component[1] == port else component[1]
                if component not in bridge:
                    build_bridge(bridge + [component], next_port)

        strength = sum(sum(component) for component in bridge)
        if strength > max_strength:
            max_strength = strength
            strongest_bridge = bridge

    build_bridge([], 0)
    return sum(sum(component) for component in strongest_bridge)


def run_part_two():
    components = parse_input()
    longest_bridge = []
    max_length = 0
    max_strength = 0

    def build_bridge(bridge: list[tuple[int, ...]], port: int) -> None:
        nonlocal max_length
        nonlocal max_strength
        nonlocal longest_bridge

        for component in components:
            if port in component:
                next_port = component[0] if component[1] == port else component[1]
                if component not in bridge:
                    build_bridge(bridge + [component], next_port)

        length = len(bridge)
        if length > max_length:
            max_length = length
            max_strength = sum(sum(component) for component in bridge)
            longest_bridge = bridge
        elif length == max_length:
            strength = sum(sum(component) for component in bridge)
            if strength > max_strength:
                max_strength = strength
                longest_bridge = bridge

    build_bridge([], 0)
    return sum(sum(component) for component in longest_bridge)


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
