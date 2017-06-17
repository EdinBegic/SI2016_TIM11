package com.timxyz.controllers;

import com.timxyz.controllers.forms.AuditItem.AuditItemSetPresentForm;
import com.timxyz.controllers.forms.AuditItem.AuditItemSetSkuCorrectForm;
import com.timxyz.controllers.forms.AuditItem.AuditItemSetStatusForm;
import com.timxyz.models.AuditItem;
import com.timxyz.models.Status;
import com.timxyz.services.AuditItemService;
import com.timxyz.services.StatusService;
import com.timxyz.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by smusic on 5/27/17.
 */

@RestController
public class AuditItemController extends BaseController<AuditItem, AuditItemService> {

    @Autowired
    private StatusService statusService;

    public void setStatusService(StatusService statusService) {
        this.statusService = statusService;
    }

    @ResponseBody
    @PostMapping("/auditItems/{id}/set-present")
    public ResponseEntity setPresent(@PathVariable("id") Long id, @RequestBody @Valid AuditItemSetPresentForm form) throws ServiceException {
        AuditItem item = service.get(id);

        if (item.getAudit().getFinished()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        item.setPresent(form.getPresent());

        return ResponseEntity.ok(service.save(item));
    }

    @ResponseBody
    @PostMapping("/auditItems/{id}/set-sku-correct")
    public ResponseEntity setSkuCorrect(@PathVariable("id") Long id, @RequestBody @Valid AuditItemSetSkuCorrectForm form) throws ServiceException {
        AuditItem item = service.get(id);

        if (item.getAudit().getFinished()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        item.setSkuCorrect(form.getSkuCorrect());

        return ResponseEntity.ok(service.save(item));
    }

    @ResponseBody
    @PostMapping("/auditItems/{id}/set-status")
    public ResponseEntity setStatus(@PathVariable("id") Long id, @RequestBody @Valid AuditItemSetStatusForm form) throws ServiceException {
        AuditItem item = service.get(id);

        if (item.getAudit().getFinished()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        item.setStatus(statusService.get(form.getStatusId()));

        return ResponseEntity.ok(service.save(item));
    }
}
