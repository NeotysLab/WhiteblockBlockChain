package com.neotys.ethereumJ.CustomActions;

import com.google.common.base.Optional;
import com.neotys.action.result.ResultFactory;
import com.neotys.ethereumJ.Web3J.Web3UtilsWhiteblock;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.Logger;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;
import java.util.Map;

import static com.neotys.action.argument.Arguments.getArgumentLogString;
import static com.neotys.action.argument.Arguments.parseArguments;

public class SendSignedTransactionActionEngine implements ActionEngine {
    private static final String STATUS_CODE_INVALID_PARAMETER = "NL-WB_SIGNEDTRANSACTION_ACTION-01";
    private static final String STATUS_CODE_TECHNICAL_ERROR = "NL-WB_SIGNEDTRANSACTION_ACTION-02";
    private static final String STATUS_CODE_BAD_CONTEXT = "NL-WB_SIGNEDACTION_ACTION-03";

    @Override
    public SampleResult execute(Context context, List<ActionParameter> parameters) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();
        final Map<String, Optional<String>> parsedArgs;
        try {
            parsedArgs = parseArguments(parameters, SendSignedTransactionOption.values());
        } catch (final IllegalArgumentException iae) {
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Could not parse arguments: ", iae);
        }



        final Logger logger = context.getLogger();
        if (logger.isDebugEnabled()) {
            logger.debug("Executing " + this.getClass().getName() + " with parameters: "
                    + getArgumentLogString(parsedArgs, SendSignedTransactionOption.values()));
        }
        final String whiteBlocMasterHost = parsedArgs.get(SendSignedTransactionOption.WhiteBlocMasterHost.getName()).get();
        final String whiteBlocMasterRpcPort = parsedArgs.get(SendSignedTransactionOption.WhiteBlocRpcPortofNode.getName()).get();

        final String from = parsedArgs.get(SendSignedTransactionOption.from.getName()).get();
        final String to=parsedArgs.get(SendSignedTransactionOption.to.getName()).get();
        final String amount=parsedArgs.get(SendSignedTransactionOption.amount.getName()).get();
        final Optional<String> publickey=parsedArgs.get(SendSignedTransactionOption.publickey.getName());
        final Optional<String> privatekey=parsedArgs.get(SendSignedTransactionOption.privatekey.getName());
        final Optional<String> traceMode=parsedArgs.get(SendSignedTransactionOption.TraceMode.getName());

        if(!isaDouble(amount))
            return ResultFactory.newErrorResult(context, STATUS_CODE_INVALID_PARAMETER, "Amout needs to be double  :"+amount, null);

        sampleResult.sampleStart();
        try
        {
            Web3UtilsWhiteblock whiteblock=new Web3UtilsWhiteblock(whiteBlocMasterHost,whiteBlocMasterRpcPort,Optional.absent(),Optional.of(from),privatekey,publickey,traceMode,context);
            String hash=whiteblock.createEtherSignedTransaction(to,amount);

            appendLineToStringBuilder(responseBuilder, "Transaction sent : hash of the transaction "+hash);
        }
        catch (Exception e)
        {
            return ResultFactory.newErrorResult(context, STATUS_CODE_BAD_CONTEXT, "Error encountered :", e);

        }


        sampleResult.sampleEnd();

        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }

    private void appendLineToStringBuilder(final StringBuilder sb, final String line){
        sb.append(line).append("\n");
    }

    /**
     * This method allows to easily create an error result and log exception.
     */
    private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
        result.setError(true);
        result.setStatusCode("NL-SendSignedTransaction_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }

    private boolean isaDouble(String amount)
    {
        try{
            double d= Double.parseDouble(amount);
            return true;

        }
        catch (NumberFormatException e)
        {
            return false;
        }

    }

    @Override
    public void stopExecute() {
        // TODO add code executed when the test have to stop.
    }

}
