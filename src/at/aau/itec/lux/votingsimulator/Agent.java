package at.aau.itec.lux.votingsimulator;

public class Agent {
    protected Party party;

    public Agent(Party party) {
        this.party = party;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
