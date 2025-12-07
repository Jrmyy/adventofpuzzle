import collections
import sys

type Point = tuple[int, int]


directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]


class GameOverException(Exception):
    pass


def reading_order(p: Point) -> tuple[int, int]:
    return p[1], p[0]


def find_closest(start: Point, targets: list[Point], excluded_nodes: list[Point]) -> tuple[list[Point], int]:
    seen = set()
    q = collections.deque([(start, 0)])
    found_dist = None
    closest = []
    while q:
        cell, dist = q.popleft()
        if found_dist is not None and dist > found_dist:
            return closest, found_dist
        if cell in seen or cell in excluded_nodes:
            continue
        seen.add(cell)
        if cell in targets:
            found_dist = dist
            closest.append(cell)
        for d in directions:
            n = (cell[0] + d[0], cell[1] + d[1])
            if n not in seen:
                q.append((n, dist + 1))
    return closest, found_dist


class Character:
    def __init__(self, position: Point, specie: str):
        self.hp = 200
        self.attack_damage = 3
        self.position = position
        self.specie = specie
        self.alive = True

    def neighbors(self) -> list[Point]:
        return [(self.position[0] + d[0], self.position[1] + d[1]) for d in directions]

    def move_to_target(self, targets: list["Character"], board: list[list[str]]) -> bool:
        excluded_nodes = [
            (x, y) for y, line in enumerate(board) for x, c in enumerate(line) if c != "." and (x, y) != self.position
        ]
        surroundings = []
        neighbours = self.neighbors()
        for target in targets:
            if target.position in neighbours:
                # This means we have at least one adjacent target, so no move
                return False
            surroundings.extend([p for p in target.neighbors() if board[p[1]][p[0]] == "."])

        closest_adjacent_to_targets, distance = find_closest(self.position, surroundings, excluded_nodes)
        if closest_adjacent_to_targets:
            choice = min(closest_adjacent_to_targets, key=reading_order)

            for n in sorted(neighbours, key=reading_order):
                _, d = find_closest(n, [choice], excluded_nodes)
                if d == distance - 1:
                    self.position = n
                    break

    def attack(self, targets: list["Character"]):
        neighbours = self.neighbors()
        potential = [t for t in targets if t.position in neighbours]
        if not potential:
            return

        target = min(potential, key=lambda c: (c.hp, reading_order(c.position)))
        if target:
            target.hp -= self.attack_damage
            target.alive = target.hp > 0


class Game:
    def __init__(self, elves_positions: list[Point], goblins_positions: list[Point], board: list[list[str]]):
        self.elves = [Character(position=pos, specie="E") for idx, pos in enumerate(elves_positions)]
        self.goblins = [Character(position=pos, specie="G") for idx, pos in enumerate(goblins_positions)]
        self._original_elves_count = len(self.elves)

        self._board = board

        self._turns = 0

    def play(self, elves_must_win: bool = False):
        while True:
            if len(self.elves) != self._original_elves_count and elves_must_win:
                raise GameOverException()
            characters = [
                c for c in sorted(self.elves + self.goblins, key=lambda c: reading_order(c.position)) if c.alive
            ]
            for character in characters:
                if character.alive:
                    targets = [t for t in (self.goblins if character.specie == "E" else self.elves) if t.alive]
                    if len(targets) == 0:
                        return
                    character.move_to_target(targets, self._board)
                    self._update_map()
                    character.attack(targets)
                    self._update_map()

                else:
                    self._update_map()

            self._turns += 1

    def _update_map(self):
        new_board = []
        self.elves = [elf for elf in self.elves if elf.alive]
        self.goblins = [goblin for goblin in self.goblins if goblin.alive]
        for y, row in enumerate(self._board):
            new_board.append([])
            for x, c in enumerate(row):
                if c == "#":
                    new_board[y].append("#")
                else:
                    elf = next((elf for elf in self.elves if elf.position == (x, y)), None)
                    goblin = next((goblin for goblin in self.goblins if goblin.position == (x, y)), None)
                    if elf and goblin:
                        raise ValueError(f"conflict on {(x, y)}")
                    new_c = "."
                    if elf:
                        new_c = "E"
                    if goblin:
                        new_c = "G"
                    new_board[y].append(new_c)
        self._board = new_board

    def outcome(self) -> int:
        if self.elves and self.goblins:
            return -1

        winners = self.elves or self.goblins
        return self._turns * sum(w.hp for w in winners)


def parse_input() -> Game:
    with open("day_15.txt") as f_in:
        lines = f_in.readlines()

    board, elves, goblins = [], [], []
    for y, line in enumerate(lines):
        board.append([])
        for x, c in enumerate(line.strip()):
            p = (x, y)
            if c == "E":
                elves.append(p)
            elif c == "G":
                goblins.append(p)
            board[y].append(c)

    return Game(elves_positions=elves, goblins_positions=goblins, board=board)


def run_part_one() -> int:
    game = parse_input()
    game.play()
    return game.outcome()


def run_part_two() -> int:
    for ap in range(4, sys.maxsize):
        game = parse_input()
        for e in game.elves:
            e.attack_damage = ap
        try:
            game.play(elves_must_win=True)
        except GameOverException:
            continue
        return game.outcome()


if __name__ == "__main__":
    print(run_part_one())
    print(run_part_two())
