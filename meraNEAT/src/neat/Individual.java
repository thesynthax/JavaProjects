package neat;

import genome.Genome;

/**
 * An individual is a single entity in a population (like a single creature)
 * Matching individuals become part of a specie
 * The compatibility of two individuals is set using the Genome.genomeCompatibility() function
 */
public class Individual
{
    //How well has the individual performed
    private double fitness;
    //Genome is the brain of an individual
    private Genome brain;

    //Number of inputs in the input layer and the outputs in the output layer of the genome (network)
    private int genomeInputs = 7, genomeOutputs = 3;

    //The vision (input) is what the individual sees
    private double[] vision = new double[genomeInputs];
    //The decision (output) is what the individual makes
    private double[] decision = new double[genomeOutputs];

    //Whether the individual dead
    private boolean dead;
    //How long the individual has lasted in a population
    private double lifeSpan;

    //Generation of this particular individual
    private int gen = 0;

    //Constructor
    public Individual()
    {
        brain = new Genome(genomeInputs, genomeOutputs);
    }

    //What the individual sees using its inputs
    public void look()
    {
        //Write code here...
    }

    //What decision does the individual makes
    public void think()
    {
        double decisionNodeValue = 0;
        int decisionNodeIndex = 0;

        decision = brain.feedForward(vision);

        for (int i = 0; i < decision.length; i++)
        {
            if (decision[i] > decisionNodeValue)
            {
                decisionNodeValue = decision[i];
                decisionNodeIndex = i;
            }
        }


        //Write code here...
    }

    public void update()
    {
        //Write code here...
    }

    //Individual copy
    public Individual clone()
    {
        Individual clone = new Individual();
        clone.brain = brain.clone();
        clone.fitness = fitness;
        clone.gen = gen;
        //Write code here...
        return clone;
    }

    //Breed
    public Individual crossOver(Individual parent2)
    {
        Individual child = new Individual();
        child.brain = brain.crossOver(parent2.brain);

        return child;
    }

    //Fitness calculator function
    public void calculateFitness()
    {
        //REPLACE
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness)
    {
        this.fitness = fitness;
    }

    public boolean isDead()
    {
        return dead;
    }

    public void setDead(boolean dead)
    {
        this.dead = dead;
    }

    public double getLifeSpan()
    {
        return lifeSpan;
    }

    public void setLifeSpan(double lifeSpan)
    {
        this.lifeSpan = lifeSpan;
    }

    public Genome getBrain()
    {
        return brain;
    }
}
