package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Nataliya on 19.05.2017.
 */
public class Task2 {
    /*
    A field simulating an infinitely large value (the distance at the first stage of the run from the initial vertex
    to all others is assumed to be equal to infinity), is used in the Dextree algorithm
     */
    private static int INF = Integer.MAX_VALUE / 2;//поле, имитирующее бесконечно большое значение (расстояние на первом этапе прогона от начальной вершины до всех остальных примимается равным бесконечности), используется в алгоритме Дейкстры
    private int cityNum; // the number of cities
    // количество городов
    private ArrayList<String> cityName; //List of city names  список с названиями городов
    private ArrayList<Integer> adjMatrix[]; //Adjacency list список смежности (связи между городами)
    private ArrayList<Integer> weightMatrix[]; //Rib weight in digraph (cost of passage between cities)
    // вес ребра в орграфе (стоимость прохода между городами)
    private boolean used[]; // An array for storing information about traversed and not passed vertices
    // массив для хранения информации о пройденных и не пройденных вершинах
    private int dist[]; //Array for storing distance from the starting point
    // массив для хранения расстояния от стартовой вершины
    private int pred[]; //Array of ancestors needed to restore the shortest path from the starting point
    // массив предков, необходимых для восстановления кратчайшего пути из стартовой вершины
    private int pathsNum; //Number of distances to be determined
    // количество расстояний, которое необходимо определить
    private int[][] pathArray;//An array containing the start and end vertices, between which the distance is sought
    // массив, содержащий стартовую и конечную вершины, между которыми ищется расстояние

    //Input of initial data
    // ввод исходных данных
    private void inputData() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Ipput the number of cities ");
        cityNum = scan.nextInt();
        cityName = new ArrayList<String>(cityNum);
        //Initialize the list of adjacencies of the graph of the dimension cityNum
        // инициализируем списка смежности графа размерности cityNum
        adjMatrix = new ArrayList[cityNum];
        for (int i = 0; i < cityNum; ++i) {
            adjMatrix[i] = new ArrayList<>();
        }
        //Initialization of the list in which the weights of the edges (cost of passage)
        // инициализация списка, в котором хранятся веса ребер (стоимости прохода)
        weightMatrix = new ArrayList[cityNum];
        for (int i = 0; i < cityNum; ++i) {
            weightMatrix[i] = new ArrayList<>();
        }
        System.out.println();
        for (int i = 0; i < cityNum; i++){
            System.out.print("Input city name, index " + Integer.toString(i+1) + ": ");
            String name = scan.next();
            cityName.add(name);
            System.out.println("-----------------------");
            System.out.print("Input the number of neighbours of city " + name + " ");
            int neighboursNum = scan.nextInt();
            for (int j = 0; j < neighboursNum; j++){
                System.out.print("Input index of a city connected to "+ name + " ");
                adjMatrix[i].add(scan.nextInt()-1);
                System.out.print("Input the transportation cost to "+ name + " ");
                weightMatrix[i].add(scan.nextInt());
            }
            System.out.println();
        }
        System.out.print("Input the number of paths to find ");
        pathsNum = scan.nextInt();
        pathArray = new int[pathsNum][2];
        for (int i = 0; i < pathsNum; i++){
            System.out.print("Input source ");
            String citySource = scan.next();
            try{
                if (cityName.indexOf(citySource) == -1) {
                    // exception generation, in case the source city is not found
                    //генерация исключения, в случае, если город-источник не найден
                    throw new IllegalArgumentException("City not found. The source city was changed to the first");
                }
                pathArray[i][0] = cityName.indexOf(citySource); // the source index is entered as the first value in the array
                // в массив в качестве первого значения в строке заносится индекс источника
            }catch (IllegalArgumentException e){ // handle the exception, change the index of the source city
                // обработка исколючения, изменение индекса города источника
                System.out.println(e);
                pathArray[i][0] = 0;
            }
            System.out.print("Input destination ");
            String cityDestination = scan.next();
            try{
                if (cityName.indexOf(cityDestination) == -1) {
                    // exception generation, in case the destination city is not found
                    //генерация исключения, в случае, если город назначения не найден
                    throw new IllegalArgumentException("City not found. The destination city was changed to the last");
                }
                pathArray[i][1] = cityName.indexOf(cityDestination); // in the array as the second value in the line, the index of the destination city
                //в массив в качестве второго значения в строке заносится индекс города назначения
            }catch (IllegalArgumentException e){ // handle the exception, change the destination city index
                //обработка исколючения, изменение индекса города назначения
                System.out.println(e);
                pathArray[i][0] = cityNum-1;
            }
        }
        System.out.println();
    }

    //Dijkstra's algorithm for finding the shortest distances from the starting vertex
    // алгоритм Дейкстры для поиска кратчайшего расстояний из стартовой вершины
    private void dejkstra(int s) {
        used = new boolean[cityNum];
        Arrays.fill(used, false);

        pred = new int[cityNum];
        Arrays.fill(pred, -1);

        dist = new int[cityNum];
        Arrays.fill(dist, INF);

        dist[s] = 0; //The shortest distance to the starting vertex is 0
        // кратчайшее расстояние до стартовой вершины равно 0
        for (int iter = 0; iter < cityNum; ++iter) {
            int v = -1; //Initially we assume that the path from the starting vertex of vertex v does not exist
            // изначально предполагаем, что пути из стартовой вершины вершины v не существует
            int distV = INF; //The distance to the vertex is infinity
            // расстояние до вершины равно бесконечности
            //Choose the vertex, the shortest distance to which has not yet been found
            // выбираем вершину, кратчайшее расстояние до которой еще не найдено
            for (int i = 0; i < cityNum; ++i) {
                if (used[i]) {
                    continue;
                }
                if (distV < dist[i]) {
                    continue;
                }
                v = i;
                distV = dist[i];
            }
            //consider all the arcs coming from the vertex
            // рассматриваем все дуги, исходящие из найденной вершины
            for (int i = 0; i < adjMatrix[v].size(); ++i) {
                int u = adjMatrix[v].get(i);
                int weightU = weightMatrix[v].get(i);
                // Relaxation of the vertex (change in distance and ancestor)
                //релаксация вершины
                if (dist[v] + weightU < dist[u]) {
                    dist[u] = dist[v] + weightU;
                    pred[u] = v;
                }
            }
            //Mark the vertex v viewed, before it was found the shortest distance
            // помечаем вершину v просмотренной, до нее найдено кратчайшее расстояние
            used[v] = true;
        }
    }

    //процедура восстановления кратчайшего пути по массиву предком
    void printWay(int v) {
        if (v == -1) {
            return;
        }
        printWay(pred[v]);
        System.out.print((v + 1) + " ");
    }

    private void outputData() {
        /*for (int v = 0; v < cityNum; ++v) {
            if (dist[v] != INF) {
                System.out.print(dist[v] + " ");
            } else {
                System.out.print("-1 ");
            }
        }
        System.out.println();
        for (int v = 0; v < cityNum; ++v) {
            System.out.print((v + 1) + ": ");
            if (dist[v] != INF) {
                printWay(v);
            }
            System.out.println();
        }*/
        for (int i = 0; i < pathsNum; i++) {
            dejkstra(pathArray[i][0]);
            if (dist[pathArray[i][1]] != INF) {
                System.out.println("The paths of minimum cost between cities is " + dist[pathArray[i][1]]);
            } else System.out.println("There is no way between pairs of cities");
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Input the number of tests ");
        int testNum = scan.nextInt();
        try{
            for (int i = 0; i < testNum; i++) {
                Task2 t = new Task2();
                t.inputData();
                System.out.println("Output");
                t.outputData();
                System.out.println();
            }
        }catch (ArrayIndexOutOfBoundsException e){
            System.out.println(e);
            System.out.println("The entered data is not allowed. Try it again");
        }
    }

}
