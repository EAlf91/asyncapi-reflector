package de.otto.asyncapidemo2.asyncreflector;

import com.asyncapi.v2.binding.ChannelBinding;
import com.asyncapi.v2.binding.OperationBinding;
import com.asyncapi.v2.binding.sqs.SQSChannelBinding;
import com.asyncapi.v2.binding.sqs.SQSOperationBinding;
import io.github.stavshamir.springwolf.asyncapi.scanners.channels.AbstractChannelScanner;
import io.github.stavshamir.springwolf.asyncapi.scanners.channels.ChannelsScanner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;

import javax.validation.Payload;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class CustomScanner extends AbstractChannelScanner<Publisher> implements ChannelsScanner, EmbeddedValueResolverAware {



    private StringValueResolver resolver;



    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected Class<Publisher> getListenerAnnotationClass() {
        return Publisher.class;
    }

    @Override
    protected String getChannelName(Publisher annotation) {
        if (annotation.value().length > 0) {
            return getChannelNameFromQueues(annotation);
        }

        return getChannelNameFromBindings(annotation);
    }

    private String getChannelNameFromQueues(Publisher annotation) {
        return Arrays.stream(annotation.value())
                .map(resolver::resolveStringValue)
                .peek(queue -> System.out.println("Resolved queue name from queue: {}"+ queue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No queue name was found in @RabbitListener annotation"));
    }

    private String getChannelNameFromBindings(Publisher annotation) {
        return Arrays.stream(annotation.value())
                .map(resolver::resolveStringValue)
                .peek(queue -> System.out.println("Resolved queue name from binding: {}"+ queue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No queue name was found in @RabbitListener annotation"));
    }

    @Override
    protected Map<String, ? extends ChannelBinding> buildChannelBinding(Publisher annotation) {

        return Map.of("sqs", new SQSChannelBinding());
    }

    private String getExchangeName(Publisher annotation) {
        String exchangeName = Stream.of(annotation.value())
                .findFirst()
                .orElse(null);

        return exchangeName;
    }

    @Override
    protected Map<String, ? extends OperationBinding> buildOperationBinding(Publisher annotation) {

        return Map.of("sqs", new SQSOperationBinding());
    }

    /*private List<String> getRoutingKeys(Publisher annotation) {

        List<String> routingKeys = Stream.of(annotation.bindings())
                .map(binding -> {
                    if (binding.key().length == 0) {
                        return Collections.singletonList("");
                    }

                    return Arrays.asList(binding.key());
                })
                .findFirst()
                .orElse(null);
        if (routingKeys == null && bindingsMap.containsKey(getChannelName(annotation))) {
            Binding binding = bindingsMap.get(getChannelName(annotation));
            routingKeys = Collections.singletonList(binding.getRoutingKey());
        }

        String exchangeName = getExchangeName(annotation);
        if (exchangeName.isEmpty() && routingKeys == null) {
            routingKeys = Collections.singletonList(getChannelName(annotation));
        }

        return routingKeys;
    }
    */
    @Override
    protected Class<?> getPayloadType(Method method) {
        String methodName = String.format("%s::%s", method.getDeclaringClass().getSimpleName(), method.getName());
        System.out.println("Finding payload type for {}" + methodName);

        Class<?>[] parameterTypes = method.getParameterTypes();
        switch (parameterTypes.length) {
            case 0:
                throw new IllegalArgumentException("Listener methods must not have 0 parameters: " + methodName);
            case 1:
                return parameterTypes[0];
            default:
                return getPayloadType(parameterTypes, method.getParameterAnnotations(), methodName);
        }
    }

    private Class<?> getPayloadType(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations, String methodName) {
        int payloadAnnotatedParameterIndex = getPayloadAnnotatedParameterIndex(parameterAnnotations);

        if (payloadAnnotatedParameterIndex == -1) {
            String msg = "Multi-parameter RabbitListener methods must have one parameter annotated with @Payload, "
                    + "but none was found: "
                    + methodName;

            throw new IllegalArgumentException(msg);
        }

        return parameterTypes[payloadAnnotatedParameterIndex];
    }

    private int getPayloadAnnotatedParameterIndex(Annotation[][] parameterAnnotations) {
        for (int i = 0, length = parameterAnnotations.length; i < length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            boolean hasPayloadAnnotation = Arrays.stream(annotations)
                    .anyMatch(annotation -> annotation instanceof Payload);

            if (hasPayloadAnnotation) {
                return i;
            }
        }

        return -1;
    }

}
