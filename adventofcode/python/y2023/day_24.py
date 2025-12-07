from sympy import *


def parse_input() -> list[list[int]]:
    hailstones = []
    with open("day_24.txt") as f_in:
        lines = f_in.readlines()
        for idx, line in enumerate(lines):
            pos, speed = line.split(" @ ")
            pos_n_speed = [int(p.strip()) for p in pos.split(", ") + speed.split(", ")]
            hailstones.append(pos_n_speed)
    return hailstones


def run_part_two() -> int:
    """
    For part 1, see aoc-go/2023/24/day.go
    Part 2:
    We take the first 3 hailstone, and we have this system of equations
    xa + ta * vxa = xr + ta * vxr
    ya + ta * vya = yr + ta * vyr
    za + ta * vza = zr + ta * vzr
    xb + tb * vxb = xr + tb * vxr
    yb + tb * vyb = yr + tb * vyr
    zb + tb * vzb = zr + tb * vzr
    xc + tc * vxc = xr + tc * vxr
    yc + tc * vyc = yr + tc * vyr
    zc + tc * vzc = zr + tc * vzr
    Once we have this system of 9 equations, we just need to solve it and return the sum of xr, yr and zr.
    """

    hailstones = parse_input()

    xa = hailstones[0][0]
    ya = hailstones[0][1]
    za = hailstones[0][2]
    vxa = hailstones[0][3]
    vya = hailstones[0][4]
    vza = hailstones[0][5]
    xb = hailstones[1][0]
    yb = hailstones[1][1]
    zb = hailstones[1][2]
    vxb = hailstones[1][3]
    vyb = hailstones[1][4]
    vzb = hailstones[1][5]
    xc = hailstones[2][0]
    yc = hailstones[2][1]
    zc = hailstones[2][2]
    vxc = hailstones[2][3]
    vyc = hailstones[2][4]
    vzc = hailstones[2][5]

    ta, tb, tc, xr, yr, zr, vxr, vyr, vzr = symbols("ta, tb, tc, xr, yr, zr, vxr, vyr, vzr")

    z = solve(
        [
            xr - xa + ta * (vxr - vxa),
            yr - ya + ta * (vyr - vya),
            zr - za + ta * (vzr - vza),
            xr - xb + tb * (vxr - vxb),
            yr - yb + tb * (vyr - vyb),
            zr - zb + tb * (vzr - vzb),
            xr - xc + tc * (vxr - vxc),
            yr - yc + tc * (vyr - vyc),
            zr - zc + tc * (vzr - vzc),
        ],
        (xr, yr, zr, ta, tb, tc, vxr, vyr, vzr),
    )

    return z[0][0] + z[0][1] + z[0][2]


if __name__ == "__main__":
    print(run_part_two())
