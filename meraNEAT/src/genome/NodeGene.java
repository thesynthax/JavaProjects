package genome;

import java.util.ArrayList;

/**
 * It is a neuron in NEAT language.
 * It contains info about the type of nodes, its output value etc
 */
public class NodeGene extends Gene
{
    //It is used for drawing the neural network
    private double xPos, yPos;

    //The type of node it is: INPUT, OUTPUT, HIDDEN or a BIAS node
    public enum NODE_TYPES { INPUT, OUTPUT, HIDDEN, BIAS }
    private NODE_TYPES nodeType;
    //Input, hidden or output layer
    private int layer;

    //It is a unique number given to each node, for identification purposes
    private int nodeID;
    /**
     * inputSum: The sum of the total output of the connections to this node
     * value: The output of this node after applying activation function.
     */
    private double inputSum, value;

    private ArrayList<ConnectionGene> inputConnections = new ArrayList<>();

    //Initializes a node with its ID, and type
    public NodeGene(int nodeID, NODE_TYPES nodeType)
    {
        this.nodeID = nodeID;
        this.nodeType = nodeType;

        this.value = (this.nodeType == NODE_TYPES.BIAS) ? 1 : 0;
    }

    //Returns a copy of the node
    public NodeGene(NodeGene copy)
    {
        this.nodeID = copy.nodeID;
        this.nodeType = copy.nodeType;

        this.value = (this.nodeType == NODE_TYPES.BIAS) ? 1 : 0;
    }

    public void calculateOutput()
    {
        double sum = 0;
        for (ConnectionGene c : inputConnections)
        {
            if (c.isEnabled())
            {
                sum += c.getWeight() * c.getInNode().getValue();
            }
        }

        value = sigmoid(sum);
    }

    private double sigmoid(double x)
    {
        return (1d / (1 + Math.exp(-4.9 * x)));
    }

    public int getNodeID()
    {
        return nodeID;
    }

    public NODE_TYPES getNodeType()
    {
        return nodeType;
    }

    public double getxPos()
    {
        return xPos;
    }

    public void setxPos(double xPos)
    {
        this.xPos = xPos;
    }

    public double getyPos()
    {
        return yPos;
    }

    public void setyPos(double yPos)
    {
        this.yPos = yPos;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        if (nodeType != NODE_TYPES.BIAS)
            this.value = value;
    }

    public double getInputSum()
    {
        return inputSum;
    }

    public void setInputSum(double inputSum)
    {
        this.inputSum = inputSum;
    }

    public void setLayer(int layer)
    {
        this.layer = layer;
    }

    public int getLayer()
    {
        return layer;
    }

    public ArrayList<ConnectionGene> getInputConnections()
    {
        return inputConnections;
    }

    //Checks the equality of two nodes by comparing their IDs
    public boolean equals(Object o)
    {
        if (!(o instanceof NodeGene))
            return false;
        return nodeID == ((NodeGene) o).getNodeID();
    }

    @Override
    public String toString()
    {
        return "" + getNodeID();
    }
}
