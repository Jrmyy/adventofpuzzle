package me.jeremy.aoc

data class Point3D(
    val x: Double,
    val y: Double,
    val z: Double
) {
    operator fun plus(point: Point3D): Point3D = Point3D(x + point.x, y + point.y, z + point.z)

    operator fun minus(point: Point3D): Point3D = Point3D(x - point.x, y - point.y, z - point.z)
}
