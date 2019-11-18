package neat;

import data_structures.RandomHashSet;
import data_structures.RandomSelector;
import genome.ConnectionGene;
import genome.Genome;
import genome.NodeGene;
import visual.Frame;

import java.util.HashMap;

/*
* The main and the most important class which will have info about all the genomes, genes, connections and nodes
 */
public class NEAT
{
    //Maximum number of possible nodes(neurons) in a network
    public static final int MAX_NODES = (int)Math.pow(2,20);

    //The coefficients for the distance formula expression
    private double C1 = 1, C2 = 1, C3 = 1;

    private double CP = 4;

    private double SURVIVORS = 0.8;

    private double WEIGHT_SHIFT_STRENGTH = 0.3;
    private double WEIGHT_RANDOM_STRENGTH = 1;

    private double PROBABILITY_MUTATE_LINK = 0.4;
    private double PROBABILITY_MUTATE_NODE = 0.4;
    private double PROBABILITY_MUTATE_WEIGHT_SHIFT = 0.4;
    private double PROBABILITY_MUTATE_WEIGHT_RANDOM= 0.4;
    private double PROBABILITY_MUTATE_TOGGLE_LINK = 0.4;

    //All the information about the node and connection genes is stored here
    private HashMap<ConnectionGene, ConnectionGene> all_connections = new HashMap<>();
    private RandomHashSet<NodeGene> all_nodes = new RandomHashSet<>();

    private RandomHashSet<Client> clients = new RandomHashSet<>();
    private RandomHashSet<Species> species = new RandomHashSet<>();

    /*
     * input_size is the number of input nodes
     * output_size is the number of output nodes
     * clients the number of genomes
     */
    private int input_size, output_size, max_clients;

    public NEAT(int input_size, int output_size, int clients)
    {
        this.reset(input_size, output_size, clients);
    }

    //Creates an empty genome with the same number of input and output nodes but no connections are present
    public Genome empty_genome()
    {
        Genome g = new Genome(this);
        for(int i = 0; i < input_size + output_size; i++)
        {
            g.getNodes().add(getNode(i + 1));
        }
        return g;
    }

    //A reset method to reset the number of nodes and genomes
    public void reset(int input_size, int output_size, int clients)
    {
        this.input_size = input_size;
        this.output_size = output_size;
        this.max_clients = clients;

        all_connections.clear();
        all_nodes.clear();
        this.clients.clear();

        /*
         * These both loops are for creating the input and output nodes based on the size assigned
         * The number of input and output nodes are independent of genomes, and are same in every genome
         */
        for (int i = 0; i < input_size; i++)
        {
            //Adds a new node
            NodeGene n = getNode();
            //Every input node has x value of 0.1
            n.setX(0.1);
            //The y value is assigned based open the index of the input node, for eg, 1st node will be at the top and 2nd will be a below it and so on...
            n.setY((i + 1) / (double)(input_size + 1));
        }

        for (int i = 0; i < output_size; i++)
        {
            //Adds a new node
            NodeGene n = getNode();
            //Every output node has x value of 0.1
            n.setX(0.9);
            //The y value is assigned based open the index of the input node, for eg, 1st node will be at the top and 2nd will be a below it and so on...
            n.setY((i + 1) / (double)(output_size + 1));
        }

        for (int i = 0; i < max_clients; i++)
        {
            Client c = new Client();
            c.setGenome(empty_genome());
            c.generate_calculator();
            this.clients.add(c);
        }
    }

    public Client getClient(int index)
    {
        return clients.get(index);
    }

    /*
     * Returns a new copy of the already existing connection 'con'
     * Used in crossover to copy the connection gene, as the child's connection gene in same as the parent's
     */
    public static ConnectionGene getConnection(ConnectionGene con)
    {
        ConnectionGene c = new ConnectionGene(con.getFrom(), con.getTo());
        c.setInnovation_number(con.getInnovation_number());
        c.setWeight(con.getWeight());
        c.setEnabled(con.isEnabled());
        return c;
    }

    //Returns a connection by its corresponding 'FROM' node and 'TO' node
    public ConnectionGene getConnection(NodeGene node1, NodeGene node2)
    {
        //Creates a new instance of a connectionGene
        ConnectionGene connectionGene = new ConnectionGene(node1, node2);

        //Check if a connection already exists
        if (all_connections.containsKey(connectionGene))
        {
            //If true, set the innovation number of this new connection to the existing connection
            connectionGene.setInnovation_number(all_connections.get(connectionGene).getInnovation_number());
        }
        else
        {
            //Else, set a new innovation number for this new connection
            connectionGene.setInnovation_number(all_connections.size() + 1);
            //Add this new connection to all the existing set of connections
            all_connections.put(connectionGene, connectionGene);
        }

        return connectionGene;
    }

    //Used for adding a new node
    public NodeGene getNode()
    {
        //Assigns a number innovation number to a new node by checking how many nodes already exist and adding 1 to it
        NodeGene n = new NodeGene(all_nodes.size() + 1);
        //Appends the all_nodes array with the new added node n
        all_nodes.add(n);
        return n;
    }

    //Returns a node by its corresponding node ID (innovation number)
    public NodeGene getNode(int id)
    {
        //Checks if the id is within the range of the number of existing nodes
        if (id <= all_nodes.size())
            //if true, returns the node corresponding to the node ID
            return all_nodes.get(id - 1);
        //Else adds a new node and then returns it
        return getNode();
    }

    public void evolve()
    {
        gen_species();
        kill();
        remove_extinct_species();
        reproduce();
        mutate();
        for (Client c : clients.getData())
        {
            c.generate_calculator();
        }
    }

    private void gen_species()
    {
        for (Species s : species.getData())
        {
            s.reset();

        }

        for (Client c : clients.getData())
        {
            if (c.getSpecies() != null)
                continue;

            boolean found = false;
            for (Species s : species.getData())
            {
                if (s.put(c))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                species.add(new Species(c));
                System.out.println(species.size());
            }
        }

        for (Species s : species.getData())
        {
            s.evaluate_score();

        }
    }

    private void kill()
    {
        for (Species s : species.getData())
        {
            s.kill(1-SURVIVORS);
        }
    }

    private void remove_extinct_species()
    {
        for (int i = species.size() - 1; i >= 0; i--)
        {

            if (species.get(i).size() <= 1)
            {
                species.get(i).goExtinct();
                species.remove(i);
            }
        }
    }

    private void reproduce()
    {
        RandomSelector<Species> selector = new RandomSelector<>();
        for (Species s : species.getData())
        {
            selector.add(s, s.getScore());

        }

        for (Client c : clients.getData())
        {
            if (c.getSpecies() == null)
            {
                Species s = selector.random();
                //System.out.println(s);
                c.setGenome(s.breed());
                s.force_put(c);
            }
        }
    }

    public void mutate()
    {
        for (Client c : clients.getData())
        {
            c.mutate();
        }
    }

    public void printSpecies()
    {
        System.out.println("###########################################");
        for (Species s : this.species.getData())
        {
            System.out.println(s + " " + s.getScore() + " " + s.size());
        }
    }

    //The MAIN method
    public static void main(String[] args)
    {
        NEAT neat = new NEAT(10,1,10);

        double[] in = new double[10];
        for (int i = 0; i < 10; i++) in[i] = Math.random();

        for (int i = 0; i < 30; i++)
        {
            for (Client c : neat.clients.getData())
            {
                double score = c.calculate(in)[0];
                c.setScore(score);
            }
            neat.evolve();
            //neat.printSpecies();
        }

        //new Frame(neat.empty_genome());
    }

    public double getC1()
    {
        return C1;
    }

    public double getC2()
    {
        return C2;
    }

    public double getC3()
    {
        return C3;
    }

    public double getWEIGHT_SHIFT_STRENGTH()
    {
        return WEIGHT_SHIFT_STRENGTH;
    }

    public double getWEIGHT_RANDOM_STRENGTH()
    {
        return WEIGHT_RANDOM_STRENGTH;
    }

    public double getPROBABILITY_MUTATE_LINK()
    {
        return PROBABILITY_MUTATE_LINK;
    }

    public double getPROBABILITY_MUTATE_NODE()
    {
        return PROBABILITY_MUTATE_NODE;
    }

    public double getPROBABILITY_MUTATE_WEIGHT_SHIFT()
    {
        return PROBABILITY_MUTATE_WEIGHT_SHIFT;
    }

    public double getPROBABILITY_MUTATE_WEIGHT_RANDOM()
    {
        return PROBABILITY_MUTATE_WEIGHT_RANDOM;
    }

    public double getPROBABILITY_MUTATE_TOGGLE_LINK()
    {
        return PROBABILITY_MUTATE_TOGGLE_LINK;
    }

    public int getOutput_size()
    {
        return output_size;
    }

    public int getInput_size()
    {
        return input_size;
    }

    public double getCP() {
        return CP;
    }

    public void setCP(double CP) {
        this.CP = CP;
    }
}
