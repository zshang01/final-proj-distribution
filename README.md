# final-proj-distribution

Online Chat application for users.

## Obeject

Provide distributed online chat system.

## Preview


## Desgin and algorithm used

Replication: Passive Replication

Graph algo: Dijkstra, Kruskal

Desing: p2p (Each Router acted as a media and clients will register on each Router base on specific algo here it is Round Robin) The message send from each client will base on the result of Dijkstra and Kruskal to find the minimum cost and send the mesage

Scalability: Round Robin to evenly distributed the client on each Router.


## Screenshots


## Intallation
```sh
$ git clone 
$ javac *.java
$ java client
```

## Authors
-  Zhou Shang

## License
This project is licensed under the MIT License - please see the [LICENSE](LICENSE)
