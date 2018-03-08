package at.aau.itec.lux.votingsimulator;

import java.util.ArrayList;
import java.util.Collections;

import static at.aau.itec.lux.votingsimulator.Voting.zipf;

public class Test {
    public static void main(String[] args) {
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
            double p = zipf(candidates.indexOf(c)+1, Voting.zipfExponent, candidates.size())/zipf(candidates.size(), Voting.zipfExponent, candidates.size());
            c.setPopularity(Math.round(p));
        }
        candidates.forEach(candidate -> System.out.println(candidate));
    }
}
