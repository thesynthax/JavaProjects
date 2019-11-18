package neat;

import genome.Genome;

public class Player
{
    private double fitness;
    private Genome brain;

    private int genomeInputs = 7, genomeOutputs = 3;

    private double[] vision = new double[genomeInputs];
    private double[] decision = new double[genomeOutputs];

    private boolean dead;
    private double lifeSpan;

    private int gen = 0;

    public Player()
    {
        brain = new Genome(genomeInputs, genomeOutputs);
    }

    public void look()
    {
        //REPLACE
    }

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


        //REPLACE
    }

    public Player clone()
    {
        Player clone = new Player();
        clone.brain = brain.clone();
        clone.fitness = fitness;
        clone.gen = gen;
        //REPLACEC
        return clone;
    }

    public Player crossOver(Player parent2)
    {
        Player child = new Player();
        child.brain = brain.crossOver(parent2.brain);

        return child;
    }

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
