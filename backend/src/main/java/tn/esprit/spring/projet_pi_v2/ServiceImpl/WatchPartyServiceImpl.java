package tn.esprit.spring.projet_pi_v2.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyCreateDTO;
import tn.esprit.spring.projet_pi_v2.DTORanim.WatchPartyResponseDTO;
import tn.esprit.spring.projet_pi_v2.Entity.WatchParty;
import tn.esprit.spring.projet_pi_v2.Mapper.WatchPartyMapper;
import tn.esprit.spring.projet_pi_v2.Repository.FeedbackRepository;
import tn.esprit.spring.projet_pi_v2.Repository.WatchPartyRepository;
import tn.esprit.spring.projet_pi_v2.ServiceInterface.IWatchPartyService;

import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class WatchPartyServiceImpl implements IWatchPartyService {

    private final WatchPartyRepository watchPartyRepository;
    private final FeedbackRepository feedbackRepository;
    private final WatchPartyMapper watchPartyMapper; // ✅ injecté

    @Override
    public WatchPartyResponseDTO createWatchParty(WatchPartyCreateDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        WatchParty wp = new WatchParty();
        wp.setTitre(dto.titre());
        wp.setClientId(userId);
        wp.setAdminId(userId);
        wp.setDateCreation(new Date());
        wp.setStatut("CREATED");
        wp.setContenuId(dto.contenuId());
        wp.getParticipantIds().add(userId);

        WatchParty saved = watchPartyRepository.save(wp);
        return watchPartyMapper.toResponseDTO(saved); // ✅ MapStruct
    }

    @Override
    public WatchPartyResponseDTO joinWatchParty(String watchPartyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        WatchParty wp = watchPartyRepository.findById(watchPartyId)
                .orElseThrow(() -> new RuntimeException("WatchParty not found"));

        if (!wp.getParticipantIds().contains(userId)) {
            wp.getParticipantIds().add(userId);
        }

        return watchPartyMapper.toResponseDTO(watchPartyRepository.save(wp)); // ✅
    }

    @Override
    public WatchPartyResponseDTO leaveWatchParty(String watchPartyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        WatchParty wp = watchPartyRepository.findById(watchPartyId)
                .orElseThrow(() -> new RuntimeException("WatchParty not found"));

        if (wp.getClientId().equals(userId)) {
            throw new RuntimeException("Host cannot leave his own WatchParty");
        }

        wp.getParticipantIds().remove(userId);
        return watchPartyMapper.toResponseDTO(watchPartyRepository.save(wp)); // ✅
    }

    @Override
    public void deleteWatchParty(String watchPartyId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        WatchParty wp = watchPartyRepository.findById(watchPartyId)
                .orElseThrow(() -> new RuntimeException("WatchParty not found"));

        if (!userId.equals(wp.getClientId()) && !userId.equals(wp.getAdminId())) {
            throw new RuntimeException("Access denied");
        }

        feedbackRepository.deleteByWatchPartyId(watchPartyId);
        watchPartyRepository.deleteById(watchPartyId);
    }

    @Override
    public WatchPartyResponseDTO getWatchPartyById(String id) {
        WatchParty wp = watchPartyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WatchParty not found"));
        return watchPartyMapper.toResponseDTO(wp); // ✅
    }

    @Override
    public List<WatchPartyResponseDTO> getAllWatchParties() {
        return watchPartyRepository.findAll()
                .stream()
                .map(watchPartyMapper::toResponseDTO) // ✅ method reference
                .toList();
    }

    // ❌ SUPPRIMER mapToDTO() manuel
}