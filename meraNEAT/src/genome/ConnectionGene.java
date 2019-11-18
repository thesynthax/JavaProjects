package genome;

/**
 * It is the connection between two nodes.
 * Carries information like innovation number, inNode, outNode, the weight of connection, enabled state etc
 */
public class ConnectionGene extends Gene
{
    /**
     * It is a special number which identifies the connection.
     * Two  same connections have the same identification/innovation number
     */
    private int innovNum;
    //The 'FROM' and the 'TO' Node
    private NodeGene inNode, outNode;

    //Weight of the connection
    private double weight;
    //Is enabled or not?
    private boolean enabled;

    //Initializes a connection with two nodes
    public ConnectionGene(NodeGene inNode, NodeGene outNode)
    {
        this.inNode = inNode;
        this.outNode = outNode;
    }

    /**
     * A Deep copy constructor.
     * Returns a copy of the ConnectionGene copy.
     * @param copy
     */
    public ConnectionGene(ConnectionGene copy)
    {
        this.inNode = copy.inNode;
        this.outNode = copy.outNode;
        this.innovNum = copy.innovNum;
        this.weight = copy.weight;
        this.enabled = copy.enabled;
    }

    public int getInnovNum()
    {
        return innovNum;
    }

    public void setInnovNum(int innovNum)
    {
        this.innovNum = innovNum;
    }

    public NodeGene getInNode()
    {
        return inNode;
    }

    public void setInNode(NodeGene inNode)
    {
        this.inNode = inNode;
    }

    public NodeGene getOutNode()
    {
        return outNode;
    }

    public void setOutNode(NodeGene outNode)
    {
        this.outNode = outNode;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    //Checks equality of two connections by checking their in and out nodes or their innovation numbers
    public boolean equals(Object o)
    {
        if (!(o instanceof ConnectionGene))
            return false;
        ConnectionGene c = (ConnectionGene) o;

        return (((inNode == c.inNode) && (outNode == c.outNode)) || (c.innovNum == this.innovNum));
    }
}
