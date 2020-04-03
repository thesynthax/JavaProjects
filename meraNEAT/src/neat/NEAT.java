package neat;

import genome.Genome;
import visual.Frame;

public class NEAT
{
    //Maximum possible nodes in the network (<1000000)
    public static final int MAX_NODES = (int)Math.pow(2, 20);

    //These coefficients are used to calculate the compatibility/distance between two genomes
    private double excessCoeff = 1, disjointCoeff = 1, weightDiffCoeff = 0.4;
    //If distance between genomes is more than this threshold, they are not compatible to be in one specie.
    private double compatibilityThreshold = 3;

    private double WEIGHT_SHIFT_STRENGTH = 0.3;
    private double WEIGHT_RANDOM_STRENGTH = 1;

    //Probability of a certain mutation to take place
    private double PROBABILITY_MUTATE_LINK = 0.09;
    private double PROBABILITY_MUTATE_NODE = 0.02;
    private double PROBABILITY_MUTATE_WEIGHT_SHIFT = 0.8;
    private double PROBABILITY_MUTATE_WEIGHT_RANDOM= 0.6;
    private double PROBABILITY_MUTATE_TOGGLE_LINK = 0.01;

    public static void main(String[] args)
    {
        NEAT neat = new NEAT();
        Genome g = new Genome(7, 3);
        g.setNeat(neat);

        new Frame(g);
    }

    public double getExcessCoeff()
    {
        return excessCoeff;
    }

    public double getDisjointCoeff()
    {
        return disjointCoeff;
    }

    public double getWeightDiffCoeff()
    {
        return weightDiffCoeff;
    }

    public double getCompatibilityThreshold()
    {
        return compatibilityThreshold;
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
}
