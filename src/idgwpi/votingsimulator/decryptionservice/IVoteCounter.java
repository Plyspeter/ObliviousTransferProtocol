package idgwpi.votingsimulator.decryptionservice;

import idgwpi.votingsimulator.dtos.ElectionResultMessage;

import java.util.ArrayList;

public interface IVoteCounter {
    void setup(ArrayList<ElectionResultMessage> results);

    void printResults();
}
