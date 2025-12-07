from math import prod

import networkx as nx


def parse_input() -> nx.Graph:
    graph = nx.Graph()
    with open("day_25.txt") as f_in:
        for line in f_in.readlines():
            v, adj = line.split(": ")
            for a in adj.strip().split(" "):
                graph.add_edge(v, a)
    return graph


def run_part_one() -> int:
    graph = parse_input()
    graph.remove_edges_from(nx.minimum_edge_cut(graph))
    return prod([len(c) for c in nx.connected_components(graph)])


if __name__ == "__main__":
    print(run_part_one())
