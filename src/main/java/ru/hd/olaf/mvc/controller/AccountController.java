package ru.hd.olaf.mvc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.hd.olaf.mvc.service.AccountService;
import ru.hd.olaf.util.JsonResponse;
import ru.hd.olaf.util.LogUtil;

import java.util.Date;

/**
 * Created by Olaf on 31.07.2017.
 */
@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getViewAccount(){
        logger.debug(LogUtil.getMethodName());
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("count", accountService.getTotalCount());

        return modelAndView;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getViewAdmin(){
        logger.debug(LogUtil.getMethodName());
        return "/admin/admin";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteData(){
        logger.debug(LogUtil.getMethodName());
        accountService.deleteData();

        return "redirect:/";
    }

    @RequestMapping(value = "/mock", method = RequestMethod.GET)
    public String mockData(){
        logger.debug(LogUtil.getMethodName());

        long startTime = System.currentTimeMillis();

        logger.debug("==============");
        logger.debug(String.format("%s: Запуск процедуры наполнения базы.", new Date(startTime)));

        accountService.mockData();

        long time = System.currentTimeMillis() - startTime;
        logger.debug(String.format("Время обработки: %s ms", time));
        logger.debug(String.format("%s: Завершение процедуры импорта данных.", new Date(System.currentTimeMillis())));

        return "redirect:/";
    }

    @RequestMapping(value = "/findEntity", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse findEntity(@RequestParam(value = "query") String query){
        logger.debug(LogUtil.getMethodName());
        return accountService.findEntity(query);
    }

    @RequestMapping(value = "/createEntity", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse createEntity(@RequestParam(value = "name") String name,
                              @RequestParam(value = "acct") String acct){
        logger.debug(LogUtil.getMethodName());

        return accountService.createEntity(name, acct);
    }

    @RequestMapping(value = "/deleteEntity", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse deleteEntity(@RequestParam(value = "id") Integer id){
        logger.debug(LogUtil.getMethodName());

        return accountService.deleteEntity(id);
    }

}
