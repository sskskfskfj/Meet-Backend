package gpt01.agent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AgentApplication

fun main(args: Array<String>) {
    runApplication<AgentApplication>(*args)
}

/*
docker run -d --rm --name livekit -p 7880:7880 -p 7881:7881/udp -e LIVEKIT_KEYS="APIrmrato3eBuJe: FZSZqW1e87vGgosFsBmtKKIVAjgDVKiK0IUIr1gp2TM" livekit/livekit-server

 */