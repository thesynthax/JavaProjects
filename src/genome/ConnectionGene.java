package genome;

import neat.NEAT;

/*
 * A connection gene is a connection between two nodes
 * It is a child class of the Gene class
 */
public class ConnectionGene extends Gene
{
    //The 'FROM' node to the 'TO' node
    private NodeGene from;
    private NodeGene to;

    //Weight of the connection
    private double weight;
    //Whether the connection is enabled or disabled
    private boolean enabled = true;

    //Connection gene initializer
    public ConnectionGene(NodeGene from, NodeGene to)
    {
        this.from = from;
        this.to = to;
    }

    public NodeGene getFrom()
    {
        return from;
    }

    public void setFrom(NodeGene from)
    {
        this.from = from;
    }

    public NodeGene getTo()
    {
        return to;
    }

    public void setTo(NodeGene to)
    {
        this.to = to;
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

    //Checks the equality of two connections by checking the from and to nodes of both of the connections
    public boolean equals(Object o)
    {
        if(!(o instanceof ConnectionGene)) return false;
        ConnectionGene c = (ConnectionGene) o;
        return (from.equals(c.from) && to.equals(c.to));
    }

    //Returns the unique Connection ID, which is the given by the below written expression
    public int hashCode()
    {
        return from.getInnovation_number() * NEAT.MAX_NODES + to.getInnovation_number();
    }
}
