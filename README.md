# scala-jld
Workspace to implement a method to support JLD input for scala.

## Tokenizer@JukaiNLP

### Construction
Tokenizer of [`JukaiNLP`](https://github.com/hshindo/JukaiNLP.jl.git). 

```julia
function Tokenizer()
    dict = IdDict(map(UTF8String, ["UNKNOWN"," ","\n"]))
    T = Float32
    embed = Embedding(T, 100, 10)
    conv = Conv(T, (10,7,), (1,100), paddims=(0,3)
    ls = Linear(T, 100, 4)
    g = @graph begin
        chars = identity(:chars)
        x = Var(reshape(chars, 1, length(chars)))
        x = embed(x)
        x = conv(x)
        x = reshape(x, size(x, 2), size(x, 3))
        x = transpose(x)
        x = relu(x)
        x = ls(x)
        x
    end
    Tokenizer(dict, IOE(), g)
end
```

### Training

```julia
t = Tokenizer()

data = readconll(PATH_TO_TRAIN_DATA, [2, 11])
train(t, 100, data)

str = "Test"
result = t(str)
```