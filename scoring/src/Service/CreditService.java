package Service;

import DAO.CreditDAO;
import Model.Credit;
import Model.Personne;
import enums.Decision;
import enums.TypeCredit;

import java.util.Date;
import java.util.List;

public class CreditService {
    private CreditDAO dao = new CreditDAO();
    private DecisionService decisionService = new DecisionService();
    private EcheanceService echeanceService = new EcheanceService();
    private EmployeService employeService = new EmployeService();
    private ProfessionnelService professionnelService = new ProfessionnelService();

    public Credit demanderCredit(int clientId, String typeClient,
                                 double montantDemande, double tauxInteret,
                                 int duree, TypeCredit typeCredit,
                                 boolean isNouveauClient) {

        // 1. Récupérer le client
        Personne client = null;
        Integer employeeId = null;
        Integer professionalId = null;

        if (typeClient.equals("EMPLOYE")) {
            client = employeService.getEmployeById(clientId);
            employeeId = clientId;
        } else if (typeClient.equals("PROFESSIONNEL")) {
            client = professionnelService.getProfessionnelById(clientId);
            professionalId = clientId;
        }

        if (client == null) {
            System.out.println("Client introuvable!");
            return null;
        }

        // 2. Créer la demande de crédit
        Credit credit = new Credit(
                employeeId,
                professionalId,
                new Date(),
                montantDemande,
                0,
                tauxInteret,
                duree,
                typeCredit,
                Decision.REFUS_AUTOMATIQUE
        );

        // 3. Prendre une décision automatique
        Decision decision = decisionService.prendreDecision(client, credit, isNouveauClient);

        // 4. Sauvegarder en BD
        int id = dao.create(credit);

        if (id > 0) {
            if (decision == Decision.ACCORDIMMEDIAT) {
                echeanceService.genererEcheances(credit);
            }
            decisionService.afficherDecision(credit, client, isNouveauClient);
        }

        return credit;
    }

    public List<Credit> getCreditsByClient(int clientId, String typeClient) {
        if (typeClient.equals("EMPLOYE")) {
            return dao.findByEmployeeId(clientId);
        } else {
            return dao.findByProfessionalId(clientId);
        }
    }


    public int ajouterCredit(Credit credit) {
        return dao.create(credit);
    }

    public Credit getCredit(int id) {
        return dao.findById(id);
    }

    public List<Credit> getAllCredits() {
        return dao.findAll();
    }

    public boolean modifierCredit(Credit credit) {
        return dao.update(credit);
    }

    public boolean supprimerCredit(int id) {
        return dao.delete(id);
    }}
