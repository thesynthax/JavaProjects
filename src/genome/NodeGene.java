package genome;

/*
 * A node is a neuron in neat language
 * It is a child class of the Gene class
 * The are input, output and hidden nodes to which connections are made from one node to another
 */
public class NodeGene extends Gene
{
    //These variables are used for drawing the Neural Network
    private double x,y;

    //Assigns an innovation number to a Node
    public NodeGene(int innovation_number)
    {
        super(innovation_number);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    //Checks the equality of two nodes by checking their innovation number
    public boolean equals(Object o)
    {
        if (!(o instanceof NodeGene)) return false;
        return innovation_number == ((NodeGene) o).getInnovation_number();
    }

    //Returns the unique Node ID, which is its innovation number
    public int hashCode()
    {
        return innovation_number;
    }
}
