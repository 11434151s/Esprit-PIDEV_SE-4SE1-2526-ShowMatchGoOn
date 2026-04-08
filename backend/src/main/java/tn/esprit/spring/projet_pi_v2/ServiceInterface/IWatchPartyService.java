package tn.esprit.spring.projet_pi_v2.ServiceInterface;

import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyCreateDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyResponseDTO;

import java.util.List;

public interface IWatchPartyService {

    WatchPartyResponseDTO createWatchParty(WatchPartyCreateDTO dto);

    WatchPartyResponseDTO joinWatchParty(String watchPartyId);

    WatchPartyResponseDTO leaveWatchParty(String watchPartyId);

    void deleteWatchParty(String watchPartyId);

    WatchPartyResponseDTO getWatchPartyById(String id);
    List<WatchPartyResponseDTO> getAllWatchParties();
}