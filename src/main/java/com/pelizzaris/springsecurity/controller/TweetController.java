package com.pelizzaris.springsecurity.controller;

import com.pelizzaris.springsecurity.controller.dto.CreateTweetDto;
import com.pelizzaris.springsecurity.controller.dto.FeedItemDto;
import com.pelizzaris.springsecurity.controller.dto.ListAllTweetDto;
import com.pelizzaris.springsecurity.entities.Role;
import com.pelizzaris.springsecurity.entities.Tweet;
import com.pelizzaris.springsecurity.repository.TweetRepository;
import com.pelizzaris.springsecurity.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping(value = "/tweet")
public class TweetController {

    private TweetRepository tweetRepository;
    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "create")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto createTweetDto, JwtAuthenticationToken jwtAuthenticationToken) {

        var user = userRepository.findById(UUID.fromString(jwtAuthenticationToken.getName())).get();
        var tweet = new Tweet();

        tweet.setUser(user.get());
        tweet.setContent(createTweetDto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteTweet (@PathVariable("id") Long id, JwtAuthenticationToken jwtAuthenticationToken){

        var user = userRepository.findById(UUID.fromString(jwtAuthenticationToken.getName())).get();

        var tweet = tweetRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
        if(isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(jwtAuthenticationToken.getName()))){
            tweetRepository.delete(tweet);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all")
    public ResponseEntity<ListAllTweetDto> listAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        var findAll = tweetRepository.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC,"creationTimestamp"))
                .map(tweet -> new FeedItemDto(tweet.getTweetId(), tweet.getContent(), tweet.getUser().getName(), tweet.getCreationTimestamp()));

        return ResponseEntity.ok(new ListAllTweetDto(findAll.getContent(), page, pageSize, findAll.getTotalPages(), findAll.getTotalElements()));
    }
}
