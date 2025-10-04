package Service;

import DAO.EmployeDAO;
import Model.Employe;

public class EmployeService {
    private EmployeDAO employeDAO = new EmployeDAO();
    private ScoringService scoringService = new ScoringService();

    public int ajouterEmploye(Employe employe) {
        scoringService.calculerScore(employe);
        return employeDAO.create(employe);
    }

    public boolean deleteEmploye(int id) {
        return employeDAO.delete(id);
    }

    public boolean modifierEmploye(Employe employe) {
        scoringService.calculerScore(employe);
        return employeDAO.update(employe);
    }

    public Employe getEmployeById(int id) {
        return employeDAO.findById(id);
    }


}
