package com.bilibili.controller;

import com.bilibili.common.JsonResponse;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.pojo.Barrage;
import com.bilibili.service.BarrageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BarrageController {
    @Autowired
    private BarrageService barrageService;
    @Autowired
    private UserSupport userSupport;

    @GetMapping("/barrages")
    public JsonResponse<List<Barrage>> getBarrages(@RequestParam Long videoId,
                                                   String startTime,
                                                   String endTime) throws Exception {
        List<Barrage> list;
        try{
            userSupport.getCurrentUserId();
            list = barrageService.getBarrages(videoId,startTime,endTime);
        }catch (Exception e){
            list = barrageService.getBarrages(videoId,null,null);
        }
        return new JsonResponse<>(list);
    }
}
