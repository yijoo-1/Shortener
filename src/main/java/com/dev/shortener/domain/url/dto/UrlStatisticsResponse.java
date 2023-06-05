package com.dev.shortener.domain.url.dto;

import com.dev.shortener.domain.url.entity.Referer;
import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.entity.UrlCall;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public record UrlStatisticsResponse(
        String shortenedUrl,
        String originUrl,
        ViewCount viewCount
) {

        public static UrlStatisticsResponse of(Url url, List<UrlCall> urlCalls) {
                ViewCount viewCount = ViewCount.from(urlCalls);
                return new UrlStatisticsResponse(url.shortenedUrl(), url.originUrl(), viewCount);
        }

        private record ViewCount(
                int totalViewCount,
                List<ViewCountPerDate> viewCountPerDate,
                List<ViewCountPerReferer> viewCountPerReferer
        ) {

                public static ViewCount from(List<UrlCall> urlCalls) {
                        int totalViewCount = urlCalls.size();
                        List<ViewCountPerDate> viewCountPerDate = ViewCountPerDate.from(urlCalls);
                        List<ViewCountPerReferer> viewCountPerReferer = ViewCountPerReferer.from(urlCalls);
                        return new ViewCount(totalViewCount, viewCountPerDate, viewCountPerReferer);
                }
        }

        private record ViewCountPerDate(LocalDate date, int viewCount) {

                public static List<ViewCountPerDate> from(List<UrlCall> urlCalls) {
                        return sortByDateAsc(calcViewCountPerDate(urlCalls));
                }

                private static Map<LocalDate, Long> calcViewCountPerDate(List<UrlCall> urlCalls) {
                        return urlCalls.stream()
                                .collect(groupingBy(urlCall -> urlCall.callTime().toLocalDate(), counting()));
                }

                private static List<ViewCountPerDate> sortByDateAsc(Map<LocalDate, Long> viewCountPerDate) {
                        return viewCountPerDate.entrySet()
                                .stream()
                                .map(entry -> new ViewCountPerDate(entry.getKey(), Math.toIntExact(entry.getValue())))
                                .sorted(comparing(ViewCountPerDate::date))
                                .collect(Collectors.toList());
                }
        }

        private record ViewCountPerReferer(
                Referer referer,
                int viewCount
        ) {

                public static List<ViewCountPerReferer> from(List<UrlCall> urlCalls) {
                        return convertToList(calcViewCountPerReferer(urlCalls));
                }

                private static Map<Referer, Long> calcViewCountPerReferer(List<UrlCall> urlCalls) {
                        return urlCalls.stream()
                                .collect(groupingBy(UrlCall::referer, counting()));
                }

                private static List<ViewCountPerReferer> convertToList(Map<Referer, Long> calcViewCountPerReferer) {
                        return calcViewCountPerReferer.entrySet()
                                .stream()
                                .map(entry -> new ViewCountPerReferer(entry.getKey(), Math.toIntExact(entry.getValue())))
                                .collect(Collectors.toList());
                }
        }
}
