package genome;

/*
 * The parent class of the Node and Connection genes.
 * Mostly holds the data about the innovation/identification number of each gene (Node/Connection)
 */
public class Gene
{
    /*
     * It is a special number given to each gene
     * It is used to uniquely identify a gene
     * It is used to know whether a gene exists in a genome or not
     * If the same gene exists in two genomes, then the same innovation number is given to the gene
     */
    protected int innovation_number;

    public Gene(int innovation_number)
    {
        this.innovation_number = innovation_number;
    }

    public Gene()
    {

    }

    public int getInnovation_number()
    {
        return innovation_number;
    }

    public void setInnovation_number(int innovation_number)
    {
        this.innovation_number = innovation_number;
    }
}
