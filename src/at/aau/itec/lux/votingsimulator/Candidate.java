package at.aau.itec.lux.votingsimulator;

public class Candidate extends Agent{
    double popularity, strategyWeight;
    int votes = 0;
    int number = 0;
    static int runningNumber = 0;

    public Candidate(Party party, double popularity) {
        super(party);
        runningNumber++;
        this.number = runningNumber;
        this.popularity = popularity;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String toString() {
        return String.format("#%02d\t%s\t%4.1f\t%4d", number, party ,popularity ,votes);
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public void addVote() {
        votes++;
    }

    public double getStrategyWeight() {
        return strategyWeight;
    }

    public void setStrategyWeight(double strategyWeight) {
        this.strategyWeight = strategyWeight;
    }
}
