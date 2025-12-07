import re
from dataclasses import dataclass


parsing_regex = r"p=<(-?\d+),(-?\d+),(-?\d+)>, v=<(-?\d+),(-?\d+),(-?\d+)>, a=<(-?\d+),(-?\d+),(-?\d+)>"


@dataclass
class Coordinates:
    x: float
    y: float
    z: float


@dataclass
class Particle:
    position: Coordinates
    speed: Coordinates
    acceleration: Coordinates

    def dist(self) -> float:
        return abs(self.position.x) + abs(self.position.y) + abs(self.position.z)


def parse_input() -> list[Particle]:
    with open("day_20.txt") as f_in:
        raw_particles = f_in.read()

    particles = []
    matches = re.finditer(parsing_regex, raw_particles, re.MULTILINE)
    for match in matches:
        groups = match.groups()
        particles.append(
            Particle(
                position=Coordinates(x=float(groups[0]), y=float(groups[1]), z=float(groups[2])),
                speed=Coordinates(x=float(groups[3]), y=float(groups[4]), z=float(groups[5])),
                acceleration=Coordinates(x=float(groups[6]), y=float(groups[7]), z=float(groups[8])),
            )
        )

    return particles


def part_one(particles: list[Particle]) -> int:
    n = 10_000

    for particle in particles:
        particle.position.x = particle.position.x + n * particle.speed.x + n * (n + 1) / 2 * particle.acceleration.x
        particle.position.y = particle.position.y + n * particle.speed.y + n * (n + 1) / 2 * particle.acceleration.y
        particle.position.z = particle.position.z + n * particle.speed.z + n * (n + 1) / 2 * particle.acceleration.z

    distances = [particle.dist() for particle in particles]
    return distances.index(min(distances))


def part_two(particles: list[Particle]) -> int:
    for n in range(1, 100):
        new_positions = [
            Coordinates(
                x=particle.position.x + n * particle.speed.x + n * (n + 1) / 2 * particle.acceleration.x,
                y=particle.position.y + n * particle.speed.y + n * (n + 1) / 2 * particle.acceleration.y,
                z=particle.position.z + n * particle.speed.z + n * (n + 1) / 2 * particle.acceleration.z,
            )
            for particle in particles
        ]

        to_remove_idx = set()
        for i1, p1 in enumerate(new_positions):
            for i2, p2 in enumerate(new_positions[i1 + 1 :]):
                if p1 == p2:
                    to_remove_idx.add(i1)
                    to_remove_idx.add(i1 + 1 + i2)

        particles = [particle for idx, particle in enumerate(particles) if idx not in to_remove_idx]

    return len(particles)


if __name__ == "__main__":
    print(part_one(parse_input()))
    print(part_two(parse_input()))
