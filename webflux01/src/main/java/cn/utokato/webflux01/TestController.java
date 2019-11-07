package cn.utokato.webflux01;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class TestController {

	/**
	 * 2018-10-22 21:36:30.130 INFO 9304 : get1 start
	 * 
	 * 2018-10-22 21:36:35.130 INFO 9304 : get1 end
	 * 
	 */
	@GetMapping("/1")
	private String oldMVC() {
		log.info("get1 start");
		String result = createStr();
		log.info("get1 end");
		return result;
	}

	/**
	 * 2018-10-22 21:36:45.644 INFO 9304 : get2 start
	 * 
	 * 2018-10-22 21:36:45.645 INFO 9304 : get2 end
	 * 
	 */
	@GetMapping("/2")
	private Mono<String> mono() {
		log.info("get2 start");
		Mono<String> result = Mono.fromSupplier(() -> createStr());
		log.info("get2 end");
		return result;
	}

	/**
	 * Flux 返回 1-N 多个对象
	 * 
	 * @return
	 */
	@GetMapping(value = "/3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	private Flux<String> flux() {
		Flux<String> result = Flux.fromStream(IntStream.range(1, 5).mapToObj(i -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "flux data :" + i;
		}));
		return result;
	}

	private String createStr() {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "some thing";
	}

}
