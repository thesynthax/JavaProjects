package genome;

import neat.NEAT;

import java.util.ArrayList;

/**
 * It contains information about the Node and Connection genes.
 * It is the brain of each client (Player in a population)
 */
public class Genome
{
    //All the connections and nodes existing in a genome
    private ArrayList<ConnectionGene> connections = new ArrayList<>();
    private ArrayList<NodeGene> nodes = new ArrayList<>();

    //Number of input and output nodes
    private int inputSize, outputSize;

    private NEAT neat;

    //Initializes a genome
    public Genome(int inputSize, int outputSize)
    {
        this.inputSize = inputSize;
        this.outputSize = outputSize;

        /**
         * Creates an empty genome: A genome having nodes but no connections
         * Creates and adds nodes to the nodes ArrayList, specifying its ID and type
         */
        connections.clear();
        nodes.clear();
        for (int i = 0; i < inputSize + outputSize + 1; i++)
        {
            if (i < inputSize)
            {
                NodeGene node = getNode(nodes.size() + 1, NodeGene.NODE_TYPES.INPUT);
                node.setxPos(0.1);
                node.setyPos((i + 1) / (double)(inputSize + 2));
                node.setLayer(0);
                nodes.add(node);
            }
            else if (i < outputSize + inputSize)
            {
                NodeGene node = getNode(nodes.size() + 1, NodeGene.NODE_TYPES.OUTPUT);
                node.setxPos(0.9);
                node.setyPos((i - inputSize + 1) / (double)(outputSize + 1));
                node.setLayer(2);
                nodes.add(node);
            }
            else
            {
                NodeGene node = getNode(nodes.size() + 1, NodeGene.NODE_TYPES.BIAS);
                node.setxPos(0.1);
                node.setyPos((i - outputSize + 1) / (double)(inputSize + 2));
                node.setLayer(0);
                nodes.add(node);
            }
        }
    }

    //Returns a copy of the node
    public NodeGene getNode(int id, NodeGene.NODE_TYPES nodeType)
    {
        NodeGene node = new NodeGene(id, nodeType);
        return node;
    }

    //Returns a node by its corresponding ID
    public NodeGene getNode(int id)
    {
        for (int i = 0; i < nodes.size(); i++)
        {
            if (nodes.get(i).getNodeID() == id)
            {
                return nodes.get(i);
            }
        }
        return null;
    }

    //Returns the bias node
    public NodeGene getBiasNode()
    {
        for (NodeGene node : nodes)
        {
            if (node.getNodeType() == NodeGene.NODE_TYPES.BIAS)
            {
                return node;
            }
        }
        return null;
    }

    //Creates a new connection and returns it
    public ConnectionGene getConn(NodeGene from, NodeGene to)
    {
        ConnectionGene connectionGene = new ConnectionGene(from, to);

        if (connections.contains(connectionGene))
            connectionGene.setInnovNum(connections.get(connectionGene.getInnovNum()).getInnovNum());
        else
        {
            connectionGene.setInnovNum(connections.size() + 1);
        }

        return connectionGene;
    }

    //Returns a connection by its corresponding ID (innovation number)
    public ConnectionGene getConn(int innovNum)
    {
        for (ConnectionGene conn : connections)
        {
            if (conn.getInnovNum() == innovNum)
                return conn;
        }
        return null;
    }

    //Returns a copy of connection con
    public ConnectionGene getConn(ConnectionGene con)
    {
        ConnectionGene connectionGene = new ConnectionGene(con.getInNode(), con.getOutNode());
        connectionGene.setInnovNum(con.getInnovNum());
        connectionGene.setWeight(con.getWeight());
        connectionGene.setEnabled(con.isEnabled());
        return connectionGene;
    }

    //Takes the input array and calculates the output
    public double[] feedForward(double... inputs)
    {
        ArrayList<NodeGene> inputNodes = new ArrayList<>(nodes.subList(0, inputSize));
        ArrayList<NodeGene> outputNodes = new ArrayList<>(nodes.subList(inputSize, inputSize + outputSize));
        ArrayList<NodeGene> hiddenNodes = new ArrayList<>(nodes.subList(inputSize + outputSize + 1, nodes.size()));
        NodeGene biasNode = getBiasNode();

        //Input nodes value set to the inputs array
        for (int i = 0; i < inputSize; i++)
            inputNodes.get(i).setValue(inputs[i]);
        //Bias node set to 1
        biasNode.setValue(1);

        for (NodeGene n : hiddenNodes)
            n.calculateOutput();

        double[] outputs = new double[outputSize];

        for (int i = 0; i < outputSize; i++)
        {
            outputNodes.get(i).calculateOutput();
            outputs[i] = outputNodes.get(i).getValue();
        }

        return outputs;
    }

    /**
     * A species is a collection of similar genomes which are used for crossover (breeding)
     * The similarity of two genomes are checked via a certain distance function which calculates the distance (similarity) between genomes
     * The more similar two genomes are, the more they belong to a specific specie, and hence they are more compatible for crossover
     * The criteria for similarity between two genomes is their connections, the weights, how much equality they share
     * The more excess and and disjoint genes two genomes have, the less evolutionary history they share,
      and the more distance they have, and the less they belong to a specie.
     */
    public double genomeCompatibility(Genome otherGenome)
    {
        //thisGenome should always have the highest innovation number
        Genome thisGenome = this;

        //Gets the highest innovation number of the each genome
        int thisGenomeHighestInnov = 0, otherGenomeHighestInnov = 0;
        if (thisGenome.getConnections().size() != 0)
            thisGenomeHighestInnov = thisGenome.getConnections().get(thisGenome.getConnections().size()-1).getInnovNum();
        if (otherGenome.getConnections().size() != 0)
            otherGenomeHighestInnov = otherGenome.getConnections().get(otherGenome.getConnections().size()-1).getInnovNum();

        //Since innovation number of thisGenome should be greater, swap the genomes if the innovation number of thisGenome is smaller
        if (thisGenomeHighestInnov < otherGenomeHighestInnov)
        {
            Genome g = thisGenome;
            thisGenome = otherGenome;
            otherGenome = g;
        }

        //These specify at what index of the connection genes we are in the thisGenome and otherGenome genomes
        int indexConnThis = 0;
        int indexConnOther = 0;

        //Number of each type of gene (similar, disjoint, excess)
        int disjoint = 0, excess = 0, similar = 0;
        //Total weight difference between the similar genes
        double weightDiff = 0;

        //This while loop will run until these indices are less than the number of connections, in each genome respectively
        while (indexConnThis < thisGenome.getConnections().size() && indexConnOther < otherGenome.getConnections().size())
        {
            //Getting the connections by their corresponding index or ID
            ConnectionGene connGeneThis = thisGenome.getConnections().get(indexConnThis);
            ConnectionGene connGeneOther = otherGenome.getConnections().get(indexConnOther);

            //Innovation number of each gene from its respective genome
            int innovThis = connGeneThis.getInnovNum();
            int innovOther = connGeneOther.getInnovNum();

            /*
            * The following if block checks whether the genes are similar or disjoint/excess
            * This is done as the genes may be similar or disjoint, but in java these genes are stored
              sequentially without having any empty spaces, hence comparing two disjoint genes is difficult.
            * The criteria to compare the genes is:
              - #1 If both the innovations are same, go to next for both
              - #2 If innovation number of gene1 is greater than gene2, go to next for gene2
              - #3 If innovation number of gene2 is greater than gene1, go to next for gene1
             */
            if (innovThis == innovOther)
            {
                //If true, then this is a similar gene, hence criteria #1
                similar ++;
                weightDiff += Math.abs(connGeneThis.getWeight() - connGeneOther.getWeight());
                indexConnThis++;
                indexConnOther++;
            }
            else if (innovThis > innovOther)
            {
                //if true, then this is a disjoint gene of gene2, hence criteria #2
                disjoint ++;
                indexConnOther++;
            }
            else
            {
                //if true, then this is a disjoint gene of gene1, hence criteria #3
                disjoint ++;
                indexConnThis ++;
            }
        }

        //This is the average weight difference
        weightDiff /= Math.max(1,similar);
        //The number of excess genes is calculated like this provided innovation number of thisGenome is greater
        excess = thisGenome.getConnections().size() - indexConnThis;

        //This factor 'N' is used for the distance formula expression
        double N = Math.max(thisGenome.getConnections().size(), otherGenome.getConnections().size());
        if (N < 20)
            N = 1;

        /**
         * The distance formula expression is:
          δ = (c1 · E / N) + (c2 · D / N) + (c3 · W)
          where δ -> distance
                E -> number of excess genes
                D -> number of disjoint genes
                W -> weight difference
                c1, c2, c3 -> some coefficients which adjust the importance of the three factors
         */
        return neat.getDisjointCoeff()  * disjoint / N + neat.getExcessCoeff() * excess / N + neat.getWeightDiffCoeff() * weightDiff;

    }

    //Breed too genomes together and return a child genome
    public Genome crossOver(Genome otherParent)
    {
        //Generate a new genome
        Genome child = new Genome(inputSize, outputSize);
        child.setNeat(new NEAT());
        //Clear all the genes
        child.getNodes().clear();
        child.getConnections().clear();

        //The connection genes in the child genome
        ArrayList<ConnectionGene> childConnGenes = new ArrayList<>();
        ArrayList<Boolean> enabledState = new ArrayList<>();

        //This loop is for assigning the connections in the child
        for (int i = 0; i < connections.size(); i++)
        {
            boolean enabled = true;

            ConnectionGene otherParentConn = getConn(i);
            if (otherParentConn != null)
            {
                if (!connections.get(i).isEnabled() || !otherParentConn.isEnabled())
                {
                    //If connections of both the parents are disabled, it is a 75% chance that it will be disabled for the child also
                    double random = Math.random();
                    if (random < 0.75)
                        enabled = false;
                }

                //Choosing genes from the parents on a random basic with equal possibility
                double random = Math.random();
                if (random < 0.5)
                    childConnGenes.add(getConnections().get(i));
                else
                    childConnGenes.add(otherParent.getConnections().get(i));
            }
            else
            {
                //If otherParent does not exist, then the connectionGenes are inherited from this genome only
                childConnGenes.add(getConnections().get(i));
                enabled = getConnections().get(i).isEnabled();
            }
            enabledState.add(enabled);
        }

        //The nodes is the parent and child are the same
        for (NodeGene node : nodes)
        {
            child.getNodes().add(new NodeGene(node));
        }

        //Add it to the connections array
        for (int i = 0; i < childConnGenes.size(); i++)
        {
            child.getConnections().add(childConnGenes.get(i));
            child.getConnections().get(i).setEnabled(enabledState.get(i));
        }

        return child;
    }

    //Checks if any connection from two genome matches by checking their innovation number
    private int matchingConnection(Genome otherParent, int innovNum)
    {
        for (int i = 0; i < otherParent.getConnections().size(); i++)
        {
            if (otherParent.getConnections().get(i).getInnovNum() == innovNum)
                return i;
        }
        return -1;
    }

    //Assigns the connections for calculating the output array
    private void resetnConnectNodes()
    {
        for (NodeGene node : nodes)
            node.getInputConnections().clear();

        for (ConnectionGene conn : connections)
            conn.getOutNode().getInputConnections().add(conn);
    }

    //Main mutate function
    public void mutate()
    {
        if (neat.getPROBABILITY_MUTATE_LINK() > Math.random())
        {
            linkMutate();
        }
        if (neat.getPROBABILITY_MUTATE_NODE() > Math.random())
        {
            nodeMutate();
        }
        if (neat.getPROBABILITY_MUTATE_WEIGHT_SHIFT() > Math.random())
        {
            weightShiftMutate();
        }
        if (neat.getPROBABILITY_MUTATE_WEIGHT_RANDOM() > Math.random())
        {
            weightRandomMutate();
        }
        if (neat.getPROBABILITY_MUTATE_TOGGLE_LINK() > Math.random())
        {
            linkToggleMutate();
        }
    }

    //Adds a new connection between two nodes
    public void linkMutate()
    {
        for (int i = 0; i < 100; i++)
        {
            NodeGene a = nodes.get((int)(Math.random() * nodes.size()));
            NodeGene b = nodes.get((int)(Math.random() * nodes.size()));

            if (a == null || b == null) continue;
            
            if (a.getxPos() == b.getxPos()){
                continue;
            }

            ConnectionGene con;
            if (a.getxPos() < b.getxPos())
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

            con = getConn(con.getInNode(), con.getOutNode());
            con.setWeight((Math.random() * 2 - 1) * neat.getWEIGHT_RANDOM_STRENGTH());
            con.setEnabled(true);
            /*if (Math.random() > 0.5)
                con.setEnabled(true);
            else
                con.setEnabled(false);*/

            addSortedConn(con);
            resetnConnectNodes();
            return;
        }
    }

    //Adds a new node between a connection, hence replacing the connection with two new ones
    public void nodeMutate()
    {
        ConnectionGene con;
        if (connections.size() > 0)
        {
            con = connections.get((int) (Math.random() * connections.size()));
        }
        else
            return;

        if (!con.isEnabled())
            return;

        NodeGene from = con.getInNode();
        NodeGene to = con.getOutNode();

        NodeGene nodeInBetween = getNode(nodes.size() + 1, NodeGene.NODE_TYPES.HIDDEN);
        nodeInBetween.setLayer(1);
        nodeInBetween.setxPos((from.getxPos() + to.getxPos())/2);
        nodeInBetween.setyPos((from.getyPos() + to.getyPos()) / 2 + Math.random() * 0.1 - 0.05);

        ConnectionGene conn1 = getConn(con);
        ConnectionGene conn2 = getConn(con);

        con.setEnabled(false);

        conn1.setWeight(1);

        conn1.setInNode(from);
        conn1.setOutNode(nodeInBetween);

        conn2.setInNode(nodeInBetween);
        conn2.setOutNode(to);

        connections.add(conn1);
        connections.add(conn2);
        nodes.add(nodeInBetween);

        resetnConnectNodes();
    }

    //Shifts the weight of a connection by some value
    public void weightShiftMutate()
    {
        ConnectionGene con;
        if (connections.size() > 0)
            con = connections.get((int)(Math.random() * connections.size()));
        else
            return;
        if (con != null)
        {
            con.setWeight(con.getWeight() + (Math.random() * 2 - 1) * neat.getWEIGHT_SHIFT_STRENGTH());
        }
    }

    //Assigns a totally random weight
    public void weightRandomMutate()
    {
        ConnectionGene con;
        if (connections.size() > 0)
            con = connections.get((int)(Math.random() * connections.size()));
        else
            return;

        if (con != null)
        {
            con.setWeight((Math.random() * 2 - 1) * neat.getWEIGHT_RANDOM_STRENGTH());
        }
    }

    //Turns on or off the connections
    public void linkToggleMutate()
    {
        ConnectionGene con;
        if (connections.size() > 0)
            con = connections.get((int)(Math.random() * connections.size()));
        else
            return;

        if (con != null)
        {
            con.setEnabled(!con.isEnabled());
        }
    }

    //A specific function made just to add a connection in the correct position wrt to its innovation
    public void addSortedConn(ConnectionGene con)
    {
        for (int i = 0; i < connections.size(); i++)
        {
            int innovation = connections.get(i).getInnovNum();
            if (con.getInnovNum() < innovation){
                connections.add(i, con);
                return;
            }
        }
        connections.add(con);
    }

    //Returns a copy of this genome
    public Genome clone()
    {
        return this;
    }

    public ArrayList<ConnectionGene> getConnections()
    {
        return connections;
    }

    public void setConnections(ArrayList<ConnectionGene> connections)
    {
        this.connections = connections;
    }

    public ArrayList<NodeGene> getNodes()
    {
        return nodes;
    }

    public void setNodes(ArrayList<NodeGene> nodes)
    {
        this.nodes = nodes;
    }

    public int getInputSize()
    {
        return inputSize;
    }

    public void setInputSize(int inputSize)
    {
        this.inputSize = inputSize;
    }

    public int getOutputSize()
    {
        return outputSize;
    }

    public void setOutputSize(int outputSize)
    {
        this.outputSize = outputSize;
    }

    public NEAT getNeat()
    {
        return neat;
    }

    public void setNeat(NEAT neat)
    {
        this.neat = neat;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Genome))
            return false;
        Genome genome = (Genome) o;

        return connections.equals(genome.getConnections());
    }
}
