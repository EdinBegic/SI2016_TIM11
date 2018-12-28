package com.timxyz;

import com.timxyz.models.Account;
import com.timxyz.models.Role;
import com.timxyz.models.Status;
import com.timxyz.services.AccountService;
import com.timxyz.services.RoleService;
import com.timxyz.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbLoader implements CommandLineRunner {

    @Autowired
    AccountService accountService;

    @Autowired
    StatusService statusService;

    @Autowired
    RoleService roleService;

    @Override
    public void run(String... strings) throws Exception {
        addStatuses();
        addRoles();
        addAccounts();
    }

    private void addRoles() {
        if (roleService.count() == 0) {
            Role roleAdmin = new Role();
            roleAdmin.setName("ROLE_ADMIN");
            roleService.save(roleAdmin);
            Role roleFinance = new Role();
            roleFinance.setName("ROLE_FINANCE");
            roleService.save(roleFinance);
            Role roleAuditTeam = new Role();
            roleAuditTeam.setName("ROLE_AUDITTEAM");
            roleService.save(roleAuditTeam);
        }
    }

    private void addAccounts() {
        if (accountService.count() == 0) {
            Role roleAdmin = roleService.find("ROLE_ADMIN");
            Account adminAccount = new Account("admin", "admin@etf.unsa.ba", "admin", "$2a$12$9hTbk5TvscTpyF4jd//PbOdpoGB5h1PpvqHO5EitHV2P9sw6.wqs2", roleAdmin, true);
            accountService.save(adminAccount);
            Role roleFinance = roleService.find("ROLE_FINANCE");
            Account financeAccount = new Account("Finance", "finance@etf.unsa.ba", "finance", "$2a$12$9hTbk5TvscTpyF4jd//PbOdpoGB5h1PpvqHO5EitHV2P9sw6.wqs2", roleFinance, true);
            accountService.save(financeAccount);
            Role roleAuditTeam = roleService.find("ROLE_AUDITTEAM");
            Account auditTeamAccount = new Account("Audit Team", "auditteam@etf.unsa.ba", "auditteam", "$2a$12$9hTbk5TvscTpyF4jd//PbOdpoGB5h1PpvqHO5EitHV2P9sw6.wqs2", roleAuditTeam, true);
            accountService.save(auditTeamAccount);
        }
    }

    private void addStatuses() {
        if (statusService.count() == 0) {
            Status statusValid = new Status();
            statusValid.setName("Ispravno");
            statusService.save(statusValid);
            Status statusInvalid = new Status();
            statusInvalid.setName("Neispravno");
            statusService.save(statusInvalid);
            Status statusDamaged = new Status();
            statusDamaged.setName("Oštećeno");
            statusService.save(statusDamaged);
        }
    }
}
