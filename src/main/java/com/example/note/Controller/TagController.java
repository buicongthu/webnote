package com.example.note.Controller;

import com.example.note.Model.Tag;
import com.example.note.Service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("Tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @GetMapping("")
    public String viewTag(Model model){
        List<Tag> alltag = tagService.alltag();
        model.addAttribute("tags",alltag);
        return "tag";
    }
    @PostMapping("")
    @ResponseBody
    public Map<String, Object> addTag( @RequestBody @Valid Tag tag, BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        if(bindingResult.hasErrors()){
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error:errorList){
                errors.add(error.getDefaultMessage());
            }
        }
        if (!errors.isEmpty()){
            response.put("errors",errors);

        }else {
            response.put("success",true);
            tagService.save(tag);
        }
        return response;
    }

    @GetMapping("/gettag/{id}")
    @ResponseBody
    public Optional<Tag> getTagbyId(@PathVariable Long id){
        return tagService.getByid(id);
    }


    @PutMapping("/update")
    @ResponseBody
    public Map<String, Object>  updatetag( @RequestBody @Valid Tag tag,Model model, BindingResult bindingResult){
        List<String> errors = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        if(bindingResult.hasErrors()){
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error:errorList){
                errors.add(error.getDefaultMessage());
            }
        }
        if (errors.isEmpty()){
            tagService.save(tag);
            response.put("success",true);
        }else {
            response.put("error",errors);
            model.addAttribute("errors",errors);
        }

        return response;
    }
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void deletetag(@PathVariable Long id){
        tagService.deletetag(id);

    }

}
