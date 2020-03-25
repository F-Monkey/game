package indi.moneky.game.controller;

import java.util.Set;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import indi.moneky.game.manager.PlayerManager;

@RestController
@RequestMapping("/user")
public class UserController {
	@Resource
	PlayerManager playerManager;

	@RequestMapping("/list")
	public Set<String> list() {
		return playerManager.getPlayers();
	}
}
