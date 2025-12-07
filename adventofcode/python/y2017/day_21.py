Image = list[list[str]]


def parse_input() -> dict[str, str]:
    with open("day_21.txt") as f_in:
        lines = f_in.readlines()

    patterns = {}
    for line in lines:
        i, o = line.strip().split(" => ")
        patterns[i] = o

    return patterns


def _generate_flips(image: Image) -> list[Image]:
    return [list(reversed(image)), [list(reversed(l)) for l in image]]


def _rotate_left(image: Image) -> Image:
    rotated_image = []
    for idx in range(len(image)):
        rotated_image.append(list(reversed([l[idx] for l in image])))
    return rotated_image


def _generate_rotates(image: Image) -> list[Image]:
    rotations = []
    image_to_rotate = image
    for _ in range(3):
        rotated = _rotate_left(image_to_rotate)
        rotations.append(rotated)
        image_to_rotate = rotated
    return rotations


def _find_enhancement_rule(sub_image: Image, patterns: dict[str, str]) -> Image:
    images_to_check_for_pattern = [
        img
        for base_img in [
            sub_image,
            *_generate_rotates(sub_image),
        ]
        for img in _generate_flips(base_img)
    ]
    patterns_to_check = [
        "/".join(["".join(line) for line in image_to_check]) for image_to_check in images_to_check_for_pattern
    ]
    for pattern in patterns_to_check:
        if matching_pattern := patterns.get(pattern):
            return [list(line) for line in matching_pattern.split("/")]

    raise Exception(f"Pattern not found for {patterns_to_check[0]}")


def generate_art(num_iterations: int) -> int:
    patterns = parse_input()
    image = [
        [".", "#", "."],
        [".", ".", "#"],
        ["#", "#", "#"],
    ]

    for _ in range(num_iterations):
        image_length = len(image)
        window_size = 2 if image_length % 2 == 0 else 3
        splits_by_side = image_length // window_size

        sub_images = [
            [
                c[window_size * split_x : window_size * (split_x + 1)]
                for c in image[window_size * split_y : window_size * (split_y + 1)]
            ]
            for split_y in range(splits_by_side)
            for split_x in range(splits_by_side)
        ]

        enhanced_sub_images = [_find_enhancement_rule(sub_image, patterns) for sub_image in sub_images]

        new_image_chunks = []
        for split in range(splits_by_side):
            chunk = enhanced_sub_images[splits_by_side * split : splits_by_side * (split + 1)]
            sub_image_length = len(chunk[0])
            merged_chunk = []
            for i in range(sub_image_length):
                merged_chunk.append([char for line in chunk for char in line[i]])
            new_image_chunks.append(merged_chunk)

        image = [line for chunk in new_image_chunks for line in chunk]

    return sum([l.count("#") for l in image])


if __name__ == "__main__":
    print(generate_art(num_iterations=5))
    print(generate_art(num_iterations=18))
