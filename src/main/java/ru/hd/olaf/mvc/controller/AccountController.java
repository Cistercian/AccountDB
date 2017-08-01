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
 *
 * Единственный контроллер....
 */
@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;      //сервисный уровень сущности accounts

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class); //логгер

    /**
     * Функция отображения главной страницы
     * @return modelAndView index.jsp
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getViewAccount(){
        logger.debug(LogUtil.getMethodName());
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("count", accountService.getTotalCount());    //на странице отображаем общее кол-во записей
                                                                            //таблицы accounts (для статистики)
        return modelAndView;
    }

    /**
     * Функция отображения страницы манипулирования данными (служебные функции очистить БД и заполнить новыми значениями)
     * @return modelAndView /admin/admin.jsp
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getViewAdmin(){
        logger.debug(LogUtil.getMethodName());
        return "/admin/admin";
    }

    /**
     * Маппинг функции очистки БД
     * @return редирект на index.jsp
     */
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteData(){
        logger.debug(LogUtil.getMethodName());
        accountService.deleteData();

        return "redirect:/";
    }

    /**
     * Маппинг функции заполнения БД значениями.
     * @return редирект на index.jsp
     */
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

    /**
     * Маппинг функции поиска данных в БД
     * @param query искомая строка (сравнивается с полями accounts.acct, clients.name)
     * @return  объект JsonResponse с результатом поиска (в виде Json объекта)
     */
    @RequestMapping(value = "/findEntity", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse findEntity(@RequestParam(value = "query") String query){
        logger.debug(LogUtil.getMethodName());
        return accountService.findEntity(query);
    }

    /**
     * Функция ручного создания записей БД
     * @param name  ФИО абонента (поле clients.name)
     * @param acct  Лицевой счет (поле account.acct)
     * @return  объект JsonResponse с результатом заведения данных (в виде Json объекта)
     */
    @RequestMapping(value = "/createEntity", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse createEntity(@RequestParam(value = "name") String name,
                              @RequestParam(value = "acct") String acct){
        logger.debug(LogUtil.getMethodName());

        return accountService.createEntity(name, acct);
    }

    /**
     * Маппинг функции ручного удаления записей БД
     * @param id    id сущности accounts ("цепное удаление" связанной сущности clients)
     * @return  объект JsonResponse с результатом удаления данных (в виде Json объекта)
     */
    @RequestMapping(value = "/deleteEntity", method = RequestMethod.POST)
    public @ResponseBody
    JsonResponse deleteEntity(@RequestParam(value = "id") Integer id){
        logger.debug(LogUtil.getMethodName());

        return accountService.deleteEntity(id);
    }

}
