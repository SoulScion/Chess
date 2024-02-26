package request_result;

import java.util.Collection;

public record ListGamesResponse(Collection<ListGamesRequest> games) {
}
