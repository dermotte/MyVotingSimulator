package at.aau.itec.lux.votingsimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Voting {

    private static final int numberOfVoters = 257;
    private static final double zipfExponent = 2;
    private static int numberOfVotes = 12;
    private static final double runs = 1000;

    public static void main(String[] args) {
        System.out.printf("Zipf exponent: %2.1f, averaging in %d runs: Difference in popularity vs. party bound voting\n", zipfExponent, (int) runs);
        System.out.println("number of votes\tSpearmans distance\toverlap in first 6 ranks");
        for (int myTries = 1; myTries <=12; myTries+=1) {

            numberOfVotes = myTries;
            // and now vote ...
            double distSum =0d;
            double intersectSum =0d;
            for (int v = 0; v< runs; v++) {
                // create 18 candidates:
                ArrayList<Candidate> candidates = new ArrayList<>(18);

                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));
                candidates.add(new Candidate(Party.Cult, 1.0));

                candidates.add(new Candidate(Party.Hack, 1.0));
                candidates.add(new Candidate(Party.Hack, 1.0));
                candidates.add(new Candidate(Party.Hack, 1.0));
                candidates.add(new Candidate(Party.Hack, 1.0));

                candidates.add(new Candidate(Party.Econ, 1.0));
                candidates.add(new Candidate(Party.Econ, 1.0));
                candidates.add(new Candidate(Party.Econ, 1.0));

                candidates.add(new Candidate(Party.Inter, 1.0));
                candidates.add(new Candidate(Party.Inter, 1.0));

                // shuffle and set popularity:
                Collections.shuffle(candidates);

                for (Candidate c : candidates) {
                    double p = zipf(candidates.indexOf(c)+1, zipfExponent, candidates.size())/zipf(candidates.size(), zipfExponent, candidates.size());
                    c.setPopularity(Math.round(p));
                }
                // candidates.forEach(candidate -> System.out.println(candidate));

                // 257 - 18 other agents :#
                // 91 Inter, 235 Cult, 120 Hack, 113 Eco, 61 None
        /*
        257	620	0,414516129
        91	38	Inter
        235	97	Cult
        120	50	Hack
        113	47	Eco
        61	25	None
         */
                ArrayList<Agent> voters = new ArrayList<>();
                for (int i = 0; i<38; i++) voters.add(new Agent(Party.Inter));
                for (int i = 0; i<97; i++) voters.add(new Agent(Party.Cult));
                for (int i = 0; i<50; i++) voters.add(new Agent(Party.Hack));
                for (int i = 0; i<47; i++) voters.add(new Agent(Party.Econ));
                for (int i = 0; i<25; i++) voters.add(new Agent(Party.None));
                for (Agent a : voters) {
                    voteStrategy(a, candidates);
                }
                ArrayList<Candidate> strategy = new ArrayList<>();
                strategy.addAll(candidates);
                Collections.sort(strategy, (o1, o2) -> o2.getVotes()-o1.getVotes());

                for (Candidate c : strategy) {
        //            System.out.println(c);
                    c.setVotes(0);
                }

                for (Agent a : voters) {
                    votePopularity(a, candidates);
                }
                ArrayList<Candidate> pop = new ArrayList<>();
                pop.addAll(candidates);
                Collections.sort(pop, (o1, o2) -> o2.getVotes()-o1.getVotes());

                for (Candidate c : pop) {
        //            System.out.println(c);
                    c.setVotes(0);
                }

                // spearmans distance ...
                double dist = 0;
                for (int i = 0; i < pop.size(); i++) {
                    Candidate c = pop.get(i);
                    int j = strategy.indexOf(c);
                    dist += Math.abs(i-j);
                }

                // jaccard bzw. schnittmenge:
                double intersect = 0;
                for (int i = 0; i < 6; i++) {
                    Candidate c = pop.get(i);
                    int j = strategy.indexOf(c);
                    if (j<6) intersect++;
                }

    //            System.out.println("dist = " + dist);
                distSum += dist;
                intersectSum += intersect;
            }
            System.out.printf("%d\t%6.2f\t%6.2f\n", numberOfVotes, distSum/ runs, intersectSum/ runs);
        }
    }

    private static void vote(Agent a, ArrayList<Candidate> candidates) {
//        votePopularity(a, candidates);
//        voteRandom(a, candidates);
        voteStrategy(a, candidates);
    }

    private static void voteStrategy(Agent a, ArrayList<Candidate> candidates) {
        if (a.getParty() == Party.None) votePopularity(a, candidates);
        else {
            ArrayList<Candidate> temp = new ArrayList<>(), draw = new ArrayList<>();
            if (true) temp.addAll(candidates);
            else {
                for (Candidate c : candidates) {
                    if (c.getParty() == a.getParty()) temp.add(c);
                }
            }
            for (Candidate c : temp) {
                if (c.getParty() == a.getParty()) {
                    c.setStrategyWeight(c.getPopularity() * 4);
                } else {
                    c.setStrategyWeight(0);
                }
            }

            for (int k = 0; k < numberOfVotes && temp.size() > 0; k++) {
                draw.clear();
                for (Candidate c : temp) {
                    for (int i = 0; i < c.getStrategyWeight(); i++) {
                        draw.add(c);
                    }
                }
                if (draw.isEmpty()) break;
                Collections.shuffle(draw);
                Candidate d = draw.remove(0);
                d.addVote();
                temp.remove(d);
            }
        }
    }

    private static void voteRandom(Agent a, ArrayList<Candidate> candidates) {
        ArrayList<Candidate> temp = new ArrayList<>();
        temp.addAll(candidates);
        Collections.shuffle(temp);
        for (int i=0; i< numberOfVotes ; i++) {
            temp.remove(0).addVote();
        }
    }

    private static void votePopularity(Agent a, ArrayList<Candidate> candidates) {
        ArrayList<Candidate> temp = new ArrayList<>(), draw = new ArrayList<>();
        temp.addAll(candidates);

        for (int k = 0; k < numberOfVotes; k++) {
            for (Candidate c: temp) {
                for (int i =0; i< c.popularity;i++) {
                    draw.add(c);
                }
            }

            Collections.shuffle(draw);
            Candidate d = draw.remove(0);
            d.addVote();
            temp.remove(d);
        }
    }

    public static double zipf(int  k, double s, int n) {
        double t1 = 1d/Math.pow(k, s);
        double t2 = 0d;
        for (int i=1; i< n; i++) {
            t2 += 1d/Math.pow(n, s);
        }
        return t1/t2;
    }
}
