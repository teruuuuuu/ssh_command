## リモートマシンのコマンドをssh経由で実行します
> java -jar JAR_PATH リモートホスト名 コマンド

### 前提条件
実行するユーザの`~/.ssh/config`にリモート接続先の設定がされているかつ、sshキーのパスフレーズなしでリモートマシンにssh接続できるものとします。
`~/.ssh/config`のファイルフォーマットは以下になります。
```
Host ホスト1
    HostName        ホスト
    Port            ポート
    IdentityFile    sshKeyファイル
    User            接続ユーザ

Host ホスト2
    HostName        ホスト
    Port            ポート
    IdentityFile    sshKeyファイル
    User            接続ユーザ
```

### 実行    
dependencyまで含めたjarがssh_command_jarにある場合は以下のように実行できます。
> java -cp "ssh_command_jar/*"  command.SshCommand ホスト コマンド