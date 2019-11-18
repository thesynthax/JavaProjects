package genome;

import calculations.Calculator;
import data_structures.RandomHashSet;
import neat.NEAT;

/*
 * A Genome is a set of Node genes and Connection genes
 */
public class Genome
{
    //The Connections and Nodes stored in RandomHashSet data structure
    private RandomHashSet<ConnectionGene> connections = new RandomHashSet<>();
    private RandomHashSet<NodeGene> nodes = new RandomHashSet<>();

    private NEAT neat;
    private Calculator calculator;

    public Genome(NEAT neat)
    {
        this.neat = neat;
    }


    /*
    * A species is a collection of similar genomes which are used for crossover (breeding)
    * The similarity of two genomes are checked via a certain distance function which calculates the distance (similarity) between genomes
    * The more similar two genomes are, the more they belong to a specific specie, and hence they are more compatible for crossover
    * The criteria for similarity between two genomes is their connections, the weights, how much equality they share
    * The more excess and and disjoint genes two genomes have, the less evolutionary history they share,
      and the more distance they have, and the less they belong to a specie.
     */
    public double distance(Genome g2)
    {
        //g1 should always have the highest innovation number
        Genome g1 = this;

        int highest_innovation_gene1 = 0;
        if (g1.getConnections().size() != 0)
        {
            highest_innovation_gene1 = g1.getConnections().get(g1.getConnections().size()-1).getInnovation_number();
        }

        int highest_innovation_gene2 = 0;
        if (g2.getConnections().size() != 0)
        {
            highest_innovation_gene2 = g2.getConnections().get(g2.getConnections().size()-1).getInnovation_number();
        }

        //Since innovation number of g1 should be greater, swap the genomes if the innovation number of g1 is smaller
        if(highest_innovation_gene1 < highest_innovation_gene2)
        {
            Genome g = g1;
            g1 = g2;
            g2 = g;
        }

        //These specify at what index of the connection genes we are in the g1 and g2 genomes
        int index_g1 = 0;
        int index_g2 = 0;

        //Number of each type of gene (similar, disjoint, excess)
        int similar = 0, disjoint = 0, excess = 0;
        //Total weight difference between the similar genes
        double weight_diff = 0;

        //This while loop will run until these indices are less than the number of connections, in each genome respectively
        while(index_g1 < g1.getConnections().size() && index_g2 < g2.getConnections().size())
        {
            //Getting the connections by their corresponding index or ID
            ConnectionGene gene1 = g1.getConnections().get(index_g1);
            ConnectionGene gene2 = g2.getConnections().get(index_g2);

            //Innovation number of each gene from its respective genome
            int in1 = gene1.getInnovation_number();
            int in2 = gene2.getInnovation_number();

            /*
            * The following if block checks whether the genes are similar or disjoint/excess
            * This is done as the genes may be similar or disjoint, but in java these genes are stored
              sequentially without having any empty spaces, hence comparing two disjoint genes is difficult.
            * The criteria to compare the genes is:
              - #1 If both the innovations are same, go to next for both
              - #2 If innovation number of gene1 is greater than gene2, go to next for gene2
              - #3 If innovation number of gene2 is greater than gene1, go to next for gene1
             */

            if (in1 == in2)
            {
                //If true, then this is a similar gene, hence criteria #1
                similar ++;
                weight_diff += Math.abs(gene1.getWeight() - gene2.getWeight());
                index_g1++;
                index_g2++;
            }
            else if(in1 > in2)
            {
                //if true, then this is a disjoint gene of gene2, hence criteria #2
                disjoint ++;
                index_g2++;
            }
            else
            {
                ///if true, then this is a disjoint gene of gene1, hence criteria #3
                disjoint ++;
                index_g1 ++;
            }
        }

        //This is the average weight difference
        weight_diff /= similar;
        //The number of excess genes is calculated like this provided innovation number of g1 is greater
        excess = g1.getConnections().size() - index_g1;

        //This factor 'N' is used for the distance formula expression
        double N = Math.max(g1.getConnections().size(), g2.getConnections().size());
        if (N < 20) N = 1;

        /*
        * The distance formula expression is:
          δ = (c1 · E / N) + (c2 · D / N) + (c3 · W)
          where δ -> distance
                E -> number of excess genes
                D -> number of disjoint genes
                W -> weight difference
                c1, c2, c3 -> some coefficients which adjust the importance of the three factors
         */
        return neat.getC1()  * disjoint / N + neat.getC2() * excess / N + neat.getC3() * weight_diff;

    }

    public static Genome crossOver(Genome g1, Genome g2)
    {
        NEAT neat = g1.getNEAT();
        Genome genome = neat.empty_genome();

        //No documentation as it is the same in distance function
        //But here we are writing the code of crossover of two genomes

        int index_g1 = 0;
        int index_g2 = 0;

        while (index_g1 < g1.getConnections().size() && index_g2 < g2.getConnections().size())
        {
            ConnectionGene gene1 = g1.getConnections().get(index_g1);
            ConnectionGene gene2 = g2.getConnections().get(index_g2);

            int in1 = gene1.getInnovation_number();
            int in2 = gene2.getInnovation_number();

            if (in1 == in2)
            {
                if (Math.random() > 0.5)
                {
                    genome.getConnections().add(neat.getConnection(gene1));
                }
                else
                {
                    genome.getConnections().add(neat.getConnection(gene2));
                }
                index_g1++;
                index_g2++;
            }
            else if (in1 > in2)
            {
                //genome.getConnections().add(neat.getConnection(gene2));
                //disjoint gene of b
                index_g2++;
            }
            else
            {
                //disjoint gene of a
                genome.getConnections().add(neat.getConnection(gene1));
                index_g1 ++;
            }
        }

        while (index_g1 < g1.getConnections().size())
        {
            ConnectionGene gene1 = g1.getConnections().get(index_g1);
            genome.getConnections().add(neat.getConnection(gene1));
            index_g1++;
        }

        //This adds nodes to the child genome
        for (ConnectionGene c:genome.getConnections().getData())
        {
            genome.getNodes().add(c.getFrom());
            genome.getNodes().add(c.getTo());
        }

        return genome;
    }

    //The master function for mutation
    public void mutate()
    {
        if(neat.getPROBABILITY_MUTATE_LINK() > Math.random())
        {
            mutate_link();
        }
        if(neat.getPROBABILITY_MUTATE_NODE() > Math.random())
        {
            mutate_node();
        }
        if(neat.getPROBABILITY_MUTATE_WEIGHT_SHIFT() > Math.random())
        {
            mutate_weight_shift();
        }
        if(neat.getPROBABILITY_MUTATE_WEIGHT_RANDOM() > Math.random())
        {
            mutate_weight_random();
        }
        if(neat.getPROBABILITY_MUTATE_TOGGLE_LINK() > Math.random())
        {
            mutate_link_toggle();
        }
    }

    //Adds a new connection between two nodes
    public void mutate_link()
    {

        for (int i = 0; i < 100; i++)
        {

            NodeGene a = nodes.random_element();
            NodeGene b = nodes.random_element();

            if(a.getX() == b.getX()){
                continue;
            }

            ConnectionGene con;
            if (a.getX() < b.getX())
            {
                con = new ConnectionGene(a,b);
            }
            else
            {
                con = new ConnectionGene(b,a);
            }

            if (connections.contains(con))
            {
                continue;
            }

            con = neat.getConnection(con.getFrom(), con.getTo());
            con.setWeight((Math.random() * 2 - 1) * neat.getWEIGHT_RANDOM_STRENGTH());

            connections.add_sorted(con);
            return;
        }
    }

    //Adds a new node between a connection, hence replacing the connection with two new ones
    public void mutate_node()
    {
        ConnectionGene con = connections.random_element();
        if(con == null) return;

        NodeGene from = con.getFrom();
        NodeGene to = con.getTo();

        NodeGene middle = neat.getNode();
        middle.setX((from.getX() + to.getX()) / 2);
        middle.setY((from.getY() + to.getY()) / 2 + Math.random() * 0.1 - 0.05);

        ConnectionGene con1 = neat.getConnection(from, middle);
        ConnectionGene con2 = neat.getConnection(middle, to);

        con1.setWeight(1);
        con2.setWeight(con.getWeight());
        con2.setEnabled(con.isEnabled());

        connections.remove(con);
        connections.add(con1);
        connections.add(con2);

        nodes.add(middle);
    }

    //Shifts the weight of a connection by some value
    public void mutate_weight_shift()
    {
        ConnectionGene con = connections.random_element();
        if (con != null)
        {
            con.setWeight(con.getWeight() + (Math.random() * 2 - 1) * neat.getWEIGHT_SHIFT_STRENGTH());
        }
    }

    //Assigns a totally random weight
    public void mutate_weight_random()
    {
        ConnectionGene con = connections.random_element();
        if (con != null)
        {
            con.setWeight((Math.random() * 2 - 1) * neat.getWEIGHT_RANDOM_STRENGTH());
        }
    }

    //Turns on or off the connections
    public void mutate_link_toggle()
    {
        ConnectionGene con = connections.random_element();
        if (con != null)
        {
            con.setEnabled(!con.isEnabled());
        }
    }

    public RandomHashSet<ConnectionGene> getConnections()
    {
        return connections;
    }

    public RandomHashSet<NodeGene> getNodes()
    {
        return nodes;
    }

    public NEAT getNEAT()
    {
        return neat;
    }
}
