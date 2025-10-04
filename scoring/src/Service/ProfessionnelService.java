package Service;

import DAO.ProfessionnelDAO;
import Model.Professionnel;

public class ProfessionnelService {
    private ProfessionnelDAO dao = new ProfessionnelDAO();
    private ScoringService scoringService = new ScoringService();


    public int ajouterProfessionnel(Professionnel p) {
        scoringService.calculerScore(p);
        return dao.create(p);
    }

    public boolean modifierProfessionnel(Professionnel p) {
        scoringService.calculerScore(p);

        return dao.update(p);
    }

    public Professionnel getProfessionnelById(int id) {
        return dao.findById(id);
    }

    public boolean supprimerProfessionnel(int id) {
        return dao.delete(id);
    }
}
